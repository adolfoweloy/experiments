package au.com.aeloy;

import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public class TestApplication {
    private HelloService helloService = new HelloService();

    public static void main(String[] args) {
        new TestApplication().runWithTimeoutPolicy();
    }

    public void runWithTimeoutPolicy() {
        TimeoutRetryPolicy timeoutRetryPolicy = new TimeoutRetryPolicy();
        timeoutRetryPolicy.setTimeout(10 * 1000);

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(timeoutRetryPolicy);

        String r = retryTemplate.execute((context) -> helloService.request());
        System.out.println(r);
    }

}
