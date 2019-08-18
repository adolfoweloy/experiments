package au.com.aeloy.retrybenchmark.api;

import au.com.aeloy.retrybenchmark.service.HelloService;
import io.github.resilience4j.retry.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.SECONDS;

@RestController
@RequestMapping("/resilience")
public class Resilience4jController {
    private static final Logger logger = LoggerFactory.getLogger(Resilience4jController.class);

    private final HelloService helloService;
    private final ScheduledExecutorService scheduler;

    public Resilience4jController(
            HelloService helloService,
            ScheduledExecutorService scheduler
    ) {
        this.helloService = helloService;
        this.scheduler = scheduler;
    }

    @GetMapping
    public CompletableFuture<String> get() {
        RetryConfig retryConfig = RetryConfig.<String>custom()
                .maxAttempts(5)
                .intervalFunction(IntervalFunction
                        .ofExponentialBackoff(SECONDS.toMillis(2), 1.2))
                .build();

        Retry retry = Retry.of("exponential-backoff", () -> retryConfig);

        logger.info("decorating the completion stage");
        Supplier<CompletionStage<String>> completionStageSupplier = Retry.decorateCompletionStage(
                retry,
                scheduler,
                () -> CompletableFuture.supplyAsync(helloService::request));

        logger.info("this should release the thread");
        return completionStageSupplier.get().toCompletableFuture();
    }

}
