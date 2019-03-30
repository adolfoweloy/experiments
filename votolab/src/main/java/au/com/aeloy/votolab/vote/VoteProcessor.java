package au.com.aeloy.votolab.vote;

import au.com.aeloy.votolab.vote.consumer.ImmutableResult;
import au.com.aeloy.votolab.vote.consumer.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class VoteProcessor {
    private static final Logger logger = LoggerFactory.getLogger(VoteProcessor.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Just emulates a message being processed.
     *
     * @param receiptHandle     handle used to delete the queue.
     * @param body              message body
     *
     * @return result after processing a vote
     */
    public Result processMessage(String receiptHandle, String body) {

        try {
            // unmarshalling data coming from message
            Vote vote = objectMapper
                    .reader()
                    .forType(Vote.class)
                    .readValue(body);

            logger.info("Processing message {}", vote);
            Result result = ImmutableResult.of(vote, receiptHandle);

            Thread.sleep(2000); // pretending that something important is being performed with this message

            return result;
        } catch (IOException | InterruptedException e) {
            // an invalid message compromise the whole system by doing it
            throw new RuntimeException(e);
        }
    }
}
