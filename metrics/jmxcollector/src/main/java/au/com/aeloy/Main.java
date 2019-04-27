package au.com.aeloy;

import com.evanlennick.retry4j.CallExecutor;
import com.evanlennick.retry4j.CallResults;
import com.evanlennick.retry4j.RetryConfig;
import com.evanlennick.retry4j.RetryConfigBuilder;
import com.evanlennick.retry4j.backoff.BackoffStrategy;
import com.evanlennick.retry4j.backoff.FixedBackoffStrategy;
import com.evanlennick.retry4j.handlers.AfterFailedTryListener;
import com.timgroup.statsd.NonBlockingStatsDClient;

import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final ServerAddress statsdServer = new ServerAddress("graphite", 8125);
    private static final ServerAddress jmxServer = new ServerAddress("microexp", 9010);

    public static void main(String[] args) throws Exception {

        Callable<JMXConnector> jmxConnectorCallable = () -> {
            try {
                JMXConnector jmxConnector = createJMXConnector();
                jmxConnector.connect();
                return jmxConnector;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        };

        // retrieves jmxconnector (already connected using a retry policy)
        BackoffStrategy backoffStrategy = new FixedBackoffStrategy();
        RetryConfig config = new RetryConfigBuilder().withBackoffStrategy(backoffStrategy).build();
        config.setMaxNumberOfTries(100);
        config.setRetryOnAnyException(true);

        CallExecutor executor = new CallExecutor(config);
        executor.registerRetryListener(new AfterFailedTryListener() {
            @Override
            public void immediatelyAfterFailedTry(CallResults results) {

                System.out.println("[RETRY] failed trying to connect to jmx server...");
            }
        });
        CallResults results = executor.execute(jmxConnectorCallable);
        JMXConnector jmxConnector = (JMXConnector) results.getResult();

        // creates the statsd collector
        StatsdCollector collector = new StatsdCollector(
                new NonBlockingStatsDClient("experiments.jmx.heap_usage",
                        statsdServer.getHostName(), statsdServer.getPort()));

        // collects metrics periodically
        scheduleMetricsCollector(() -> {
            try {
                CompositeData cd = (CompositeData) jmxConnector.getMBeanServerConnection().getAttribute(
                        new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage");
                Gauge gauge = new Gauge("heap.usage", Long.parseLong(cd.get("used").toString()));
                collector.send(gauge);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private static JMXConnector createJMXConnector() throws IOException {
        String url = String.format(
                "service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi",
                jmxServer.getHostName(), jmxServer.getPort());

        return JMXConnectorFactory.connect(new JMXServiceURL(url));
    }

    private static void scheduleMetricsCollector(Runnable collector) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(
                collector, 30, 500, TimeUnit.SECONDS);
    }
}
