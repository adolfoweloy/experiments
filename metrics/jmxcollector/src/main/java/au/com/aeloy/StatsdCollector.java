package au.com.aeloy;

import com.timgroup.statsd.NonBlockingStatsDClient;

public class StatsdCollector {
    private final NonBlockingStatsDClient statsDClient;

    public StatsdCollector(NonBlockingStatsDClient statsDClient) {
        this.statsDClient = statsDClient;
    }

    public void send(Gauge gauge) {
        // logic to send metrics to statsd server
        statsDClient.recordGaugeValue(gauge.getKey(), gauge.getValue());
    }
}
