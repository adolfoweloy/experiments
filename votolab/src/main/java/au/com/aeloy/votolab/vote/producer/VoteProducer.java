package au.com.aeloy.votolab.vote.producer;

import au.com.aeloy.votolab.vote.Vote;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vote")
public class VoteProducer {

    private final AmazonSQS amazonSQS;
    private final ObjectMapper objectMapper;

    public VoteProducer(AmazonSQS amazonSQS, ObjectMapper objectMapper) {
        this.amazonSQS = amazonSQS;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/")
    public ResponseEntity<?> send(@RequestBody Vote vote) throws JsonProcessingException {
        GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest();
        getQueueUrlRequest.setQueueName("vote");
        GetQueueUrlResult queueUrl = amazonSQS.getQueueUrl(getQueueUrlRequest);

        SendMessageRequest sendMessageRequest = new SendMessageRequest();
        sendMessageRequest.setQueueUrl(queueUrl.getQueueUrl());
        sendMessageRequest.setMessageBody(objectMapper.writeValueAsString(vote));

        SendMessageResult sendMessageResult = amazonSQS.sendMessage(sendMessageRequest);
        System.out.println("message ID:" + sendMessageResult.getMessageId());

        return ResponseEntity.ok().build();
    }

}
