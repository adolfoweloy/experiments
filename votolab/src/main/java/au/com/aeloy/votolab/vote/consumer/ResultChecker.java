package au.com.aeloy.votolab.vote.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class ResultChecker {
    private static final Logger logger = LoggerFactory.getLogger(ResultChecker.class);

    public void checkAll(LinkedList<Future<Result>> results)
            throws InterruptedException, ExecutionException
    {
        while (!results.isEmpty()) {
            Thread.sleep(2000); // wait a moment before checking the future result

            Future<Result> r = results.poll();
            logger.info("Checking if the future {} is ready", r.hashCode());
            if (r.isDone()) {
                logger.info("Message {} processed", r.get().messageId());
            } else {
                // rotating the results
                results.addLast(r);
            }
        }
    }

}
