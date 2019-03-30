package au.com.aeloy.votolab;

import au.com.aeloy.votolab.vote.consumer.VoteConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class VotolabApplication {
	private static final Logger logger = LoggerFactory.getLogger(VotolabApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication
				.run(VotolabApplication.class, args);

		// starts the polling method
		new VotolabApplication().startPolling(applicationContext);
	}


	private void startPolling(ApplicationContext context) {
		VoteConsumer consumer = context.getBean(VoteConsumer.class);

		// pooling for SQS messages every 7 seconds
		while (true) try {
			Thread.sleep(7000);

			logger.info("Receiving messages / processing...");
			consumer.receiveMessage();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}
