package au.com.aeloy.votolab;

import au.com.aeloy.votolab.vote.consumer.VoteConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class VotolabApplication {
	private ApplicationContext context;

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication
				.run(VotolabApplication.class, args);

		VotolabApplication me = new VotolabApplication(applicationContext);
		me.startPolling();
	}

	VotolabApplication(ApplicationContext applicationContext) {
		this.context = applicationContext;
	}

	private void startPolling() {
		ExecutorService executorService = Executors.newFixedThreadPool(5);

		while (true) {
			try {
				System.out.println("polling...");
				Thread.sleep(1000);

				VoteConsumer consumer = context.getBean(VoteConsumer.class);

				// start polling
				System.out.println("receiving message");
				executorService.submit(consumer::receiveMessage);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
