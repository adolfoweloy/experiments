package au.com.aeloy.retrybenchmark.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
public class RetryConfiguration {

    @Bean
    RetryTemplate retryTemplate(RetryLogger retryLogger) {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(SECONDS.toMillis(2));
        backOffPolicy.setMaxInterval(SECONDS.toMillis(10));
        backOffPolicy.setMultiplier(1.2);

        TimeoutRetryPolicy timeoutRetryPolicy = new TimeoutRetryPolicy();
        timeoutRetryPolicy.setTimeout(SECONDS.toMillis(60));

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(timeoutRetryPolicy);

        retryTemplate.setListeners(new RetryListener[]{ retryLogger });

        return retryTemplate;
    }

    @Bean
    ScheduledExecutorService scheduledRetry() {
        return Executors.newScheduledThreadPool(500);
    }
}
