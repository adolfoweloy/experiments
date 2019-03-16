package au.com.aeloy.asyncexperiments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {

    @Bean
    RetryTemplate retryTemplate() {
        TimeoutRetryPolicy timeoutRetryPolicy = new TimeoutRetryPolicy();
        timeoutRetryPolicy.setTimeout(5000);

        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        exponentialBackOffPolicy.setInitialInterval(1000);
        exponentialBackOffPolicy.setMaxInterval(4000);
        exponentialBackOffPolicy.setMultiplier(1.1);

        RetryTemplate retry = new RetryTemplate();

        retry.setRetryPolicy(timeoutRetryPolicy);
        retry.setBackOffPolicy(exponentialBackOffPolicy);

        return retry;
    }
}
