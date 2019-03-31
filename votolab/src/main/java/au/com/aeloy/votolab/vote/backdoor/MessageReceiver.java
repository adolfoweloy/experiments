package au.com.aeloy.votolab.vote.backdoor;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class MessageReceiver {

    private final AmazonSQS amazonSQS;

    public MessageReceiver(AmazonSQS amazonSQS) {
        this.amazonSQS = amazonSQS;
    }

    @Async
    public CompletableFuture<List<Message>> receiveMessages(ReceiveMessageRequest request) {
        CompletableFuture<List<Message>> future = new CompletableFuture<>();
        ReceiveMessageResult result = amazonSQS.receiveMessage(request);
        future.complete(result.getMessages());
        return future;
    }

}
