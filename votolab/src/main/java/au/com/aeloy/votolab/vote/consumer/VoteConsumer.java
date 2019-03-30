package au.com.aeloy.votolab.vote.consumer;

import au.com.aeloy.votolab.util.CustomCollectors;
import au.com.aeloy.votolab.vote.VoteProcessor;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class VoteConsumer {
    private static final Logger logger = LoggerFactory.getLogger(VoteConsumer.class);
    private static final int MAX_NUMBER_OF_MESSAGES = 10;

    private final AmazonSQS amazonSQS;
    private final String queueName;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final ResultChecker resultChecker;
    private final VoteProcessor voteProcessor;

    public VoteConsumer(
            AmazonSQS amazonSQS,
            @Qualifier("queueName") String queueName,
            ResultChecker resultChecker,
            VoteProcessor voteProcessor) {
        this.amazonSQS = amazonSQS;
        this.queueName = queueName;
        this.resultChecker = resultChecker;
        this.voteProcessor = voteProcessor;
    }

    /**
     * Send a ReceiveMessage action to Amazon SQS and process
     * received messages on separate threads. The way this application was designed
     * it is not ideal for horizontal scaling. To better allow for horizontal scaling,
     * the ideal approach would be to have a separate application running as a consumer.
     */
    public void receiveMessage() throws InterruptedException, ExecutionException {
        ReceiveMessageRequest request = createReceiveMessageRequest();
        ReceiveMessageResult messageResult = amazonSQS.receiveMessage(request);

        logger.info("Total of messages retrieved: {}",  messageResult.getMessages().size());
        LinkedList<Future<Result>> results = messageResult.getMessages().stream()
                .map(this::processMessageFunction)
                .collect(CustomCollectors.collectAsLinkedList());

        resultChecker.checkAll(results);
    }

    private ReceiveMessageRequest createReceiveMessageRequest() {
        ReceiveMessageRequest request = new ReceiveMessageRequest();
        request.setQueueUrl(queueName);
        request.setMaxNumberOfMessages(MAX_NUMBER_OF_MESSAGES);
        return request;
    }

    /**
     * Submit a callable in order to process the message in another thread.
     * @param message   message to be processed
     * @return {@code Future<Result>} being processed asynchronously.
     */
    private Future<Result> processMessageFunction(Message message) {
        String receiptHandle = message.getReceiptHandle();
        return executorService.submit(
                () -> {
                    Result result = voteProcessor.processMessage(
                            receiptHandle,
                            message.getBody());

                    logger.info("Deleting message {}.", receiptHandle);
                    amazonSQS.deleteMessage(queueName, receiptHandle);

                    return result;
                });
    }

}
