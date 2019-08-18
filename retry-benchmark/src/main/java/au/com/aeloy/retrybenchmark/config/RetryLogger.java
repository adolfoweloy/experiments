package au.com.aeloy.retrybenchmark.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

@Component
class RetryLogger implements RetryListener {
    private static final Logger logger = LoggerFactory.getLogger(RetryLogger.class);

    @Override
    public <T, E extends Throwable> boolean open(
            RetryContext context,
            RetryCallback<T, E> callback
    ) {
        logger.info("starting to execute");
        return true;
    }

    @Override
    public <T, E extends Throwable> void close(
            RetryContext context,
            RetryCallback<T, E> callback,
            Throwable throwable
    ) {
        logger.info("stopping retry");
    }

    @Override
    public <T, E extends Throwable> void onError(
            RetryContext context,
            RetryCallback<T, E> callback,
            Throwable throwable
    ) {
//        if (context.getRetryCount() == 15) {
//            throw new ExhaustedRetryException("Attempted for 15 times. Time to give up");
//        }
        logger.info("retrying {}", context.getRetryCount());
    }
}
