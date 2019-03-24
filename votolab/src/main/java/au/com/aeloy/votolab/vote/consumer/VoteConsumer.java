package au.com.aeloy.votolab.vote.consumer;

import au.com.aeloy.votolab.vote.Vote;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
public class VoteConsumer {

    private final AmazonSQS amazonSQS;
    private final ObjectMapper objectMapper;

    public VoteConsumer(AmazonSQS amazonSQS, ObjectMapper objectMapper) {
        this.amazonSQS = amazonSQS;
        this.objectMapper = objectMapper;
    }

    public void receiveMessage() {
        // it's being unnecessarily executed too many times. could be executed just once
        GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest();
        getQueueUrlRequest.setQueueName("payments");
        String queueUrl =amazonSQS
                .getQueueUrl(getQueueUrlRequest)
                .getQueueUrl();

        ReceiveMessageResult messageResult = amazonSQS.receiveMessage(queueUrl);

        System.out.println("processing messages " + messageResult.getMessages().size());
        Set<Result> voteMessages = messageResult.getMessages().stream()
                .map(message -> processMessage(
                        queueUrl,
                        message.getReceiptHandle(),
                        message.getBody()))
                .collect(toSet());

        // processing messages
        voteMessages.forEach(System.out::println);
    }

    private Result processMessage(String queueUrl, String receiptHandle, String body) {
        try {
            Vote vote = objectMapper
                    .reader()
                    .forType(Vote.class)
                    .readValue(body);

            Result result = ImmutableResult.of(vote, receiptHandle);

            Thread.sleep(2000);
            System.out.println("message processed, deleting it");
            amazonSQS.deleteMessage(queueUrl, receiptHandle);

            return result;
        } catch (IOException | InterruptedException e) {
            // an invalid message compromise the whole system by doing it
            throw new RuntimeException(e);
        }
    }

}
