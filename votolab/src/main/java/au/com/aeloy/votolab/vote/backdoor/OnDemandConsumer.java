package au.com.aeloy.votolab.vote.backdoor;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Extremely contrived example, I know.
 * The endpoint provided by this controller, allows to play with short polling process of message consuming.
 */
@RestController
@RequestMapping("/backdoor")
public class OnDemandConsumer {
    private static final Logger logger = LoggerFactory.getLogger(OnDemandConsumer.class);

    private final AmazonSQS amazonSQS;
    private final MessageReceiver messageReceiver;
    private final String queueName;

    public OnDemandConsumer(
            AmazonSQS amazonSQS,
            MessageReceiver messageReceiver,
            String queueName
    ) {
        this.amazonSQS = amazonSQS;
        this.messageReceiver = messageReceiver;
        this.queueName = queueName;
    }

    @PostMapping("/consume")
    public ResponseEntity<Void> consume(
            @RequestParam("long-polling") String longPolling) {

        // preparing receive message request action
        ReceiveMessageRequest request = getReceiveMessageRequest(
                Optional.ofNullable(longPolling).orElse("false"));

        // running receive message action
        messageReceiver
                .receiveMessages(request)
                .thenAcceptAsync((messages) -> {
                    logger.info("received {} message(s)", messages.size());
                    messages.stream().forEach(m -> {
                        logger.info("message: {}", m.getBody());
                        logger.info("handler: {}", m.getMessageId());

                        // deleting the message
                        amazonSQS.deleteMessage(queueName, m.getReceiptHandle());
                        logger.info("message {} deleted", m.getMessageId());
                    });
                });

        return ResponseEntity.ok().build();
    }

    private ReceiveMessageRequest getReceiveMessageRequest(String longPolling) {
        ReceiveMessageRequest request = new ReceiveMessageRequest();

        if (!"false".equals(longPolling)) {
            logger.info("using {} seconds for long polling", 15);
            request.setWaitTimeSeconds(15);
        }

        request.setQueueUrl(queueName);
        request.setMaxNumberOfMessages(4);
        return request;
    }

}
