package au.com.aeloy.votolab.vote.backdoor;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Extremely contrived example, I know.
 * The endpoint provided by this controller, allows to play with short polling process of message consuming.
 */
@RestController
@RequestMapping("/backdoor")
public class OnDemandConsumer {
    private static final Logger logger = LoggerFactory.getLogger(OnDemandConsumer.class);

    private final AmazonSQS amazonSQS;

    public OnDemandConsumer(AmazonSQS amazonSQS) {
        this.amazonSQS = amazonSQS;
    }

    @PostMapping("/consume")
    public ResponseEntity<Void> consume() {

        // get queue url request action
        GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest();
        getQueueUrlRequest.setQueueName("vote");
        String queueUrl =amazonSQS
                .getQueueUrl(getQueueUrlRequest)
                .getQueueUrl();

        // preparing receive message request action
        ReceiveMessageRequest request = new ReceiveMessageRequest();
        request.setQueueUrl(queueUrl);
        request.setMaxNumberOfMessages(10);

        // running receive message action
        ReceiveMessageResult result = amazonSQS.receiveMessage(request);

        // handling the result(s)
        // Amazon SQS stores messages on multiple nodes. When sending the receiveMessage action,
        // SQS samples a random number of nodes to collect messages, so it might not retrieve all
        // messages if you have less than 1000 messages in your queue.
        List<Message> messages = result.getMessages();
        messages.stream().forEach(m -> {
            logger.info("message: {}", m.getBody());
            logger.info("handler: {}", m.getReceiptHandle());
        });

        // deleting message
        // by not deleting possible other messages retrieved, they will remain in flight
        // until their visibility timeout expires.
        amazonSQS.deleteMessage(queueUrl, messages.get(0).getReceiptHandle());
        logger.info("messages deleted");

        return ResponseEntity.ok().build();
    }
}
