package au.com.aeloy.votolab.aws;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SQSConfig {

    private static final Logger logger = LoggerFactory.getLogger(SQSConfig.class);

    @Bean
    public AmazonSQS amazonSQS() {
        return AmazonSQSClientBuilder.standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build();
    }

    @Bean
    public String queueName(AmazonSQS amazonSQS) {
        GetQueueUrlRequest request = new GetQueueUrlRequest();
        request.withQueueName("vote");
        request.withGeneralProgressListener(
                (x) -> logger.debug("GetQueueUrlRequest for [vote]: {}", x.getEventType()));

        GetQueueUrlResult result = amazonSQS.getQueueUrl(request);
        return result.getQueueUrl();
    }

}
