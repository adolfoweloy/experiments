package au.com.aeloy;

import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class TestWithExponentialBackoff {
    private HelloService helloService = new HelloService();

    public static void main(String[] args) {
        new TestWithExponentialBackoff().runWithExponentialBackoff();
    }

    void runWithExponentialBackoff() {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(SECONDS.toMillis(2));
        backOffPolicy.setMaxInterval(SECONDS.toMillis(30));
        backOffPolicy.setMultiplier(1.2);

        TimeoutRetryPolicy retryPolicy = new TimeoutRetryPolicy();
        retryPolicy.setTimeout(MINUTES.toMillis(2));

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);

        String r = retryTemplate.execute((context) -> helloService.request());
        System.out.println(r);
    }
}
