package au.com.aeloy.asyncexperiments.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class Payments {
    private static final Logger logger = LoggerFactory.getLogger(Payments.class);

    private final AvailablePaymentMethods availablePaymentMethods;

    public Payments(AvailablePaymentMethods availablePaymentMethods) {
        this.availablePaymentMethods = availablePaymentMethods;
    }

    @Async
    void send(Payment payment) {
        try {
            validatePaymentMethodAgainstLocale(payment);

            // what happens with the caller? does it have to wait 4 seconds?
            // NO, the caller won't wait.

            // what happens with the controller? does it wait 4 seconds?
            // NO, the controller doesn't need to wait.

            // what happens when the response is returned?
            // Async annotated methods can just return nothing (void) or Future (or ListenableFuture or CompletableFuture).
            Thread.sleep(4000);
            logger.debug("finished waiting");
        } catch (InterruptedException e) {
            // async methods run on a different thread. So the main thread can't handle this exception.
            // exceptions in this case can be logged only or handled by AsyncUncaughtExceptionHandler
            // AsyncUncaughtExceptionHandler can be implemented and returned from a configuration class
            // used to enableAsync. This configuration class can implement AsyncConfigurer or you can also
            // extend AsyncConfigurerSupport.
            throw new RuntimeException("exception occurred while processing async operation", e);
        }
    }

    @Async
    CompletableFuture<String> process(Payment payment) {
        try {
            CompletableFuture<String> future = new CompletableFuture<>();

            validatePaymentMethodAgainstLocale(payment);

            logger.debug("going to sleep");
            Thread.sleep(4000);
            logger.debug("woke up");
            future.complete("WAITING_PROCESS");

            return future;
        } catch (InterruptedException e) {
            throw new RuntimeException("exception ocurred while processing async operation", e);
        }
    }

    private void validatePaymentMethodAgainstLocale(Payment payment) {
        // condition to throw an exception.
        // the exception being thrown here won't be handled if there is no AsyncUncaughtExceptionHandler
        if (!availablePaymentMethods.paymentMethodFor(payment.getLocale())
                .contains(payment.getPaymentMethod())) {

            // don't log redundant things related to the exception being thrown
            logger.error("This can be logged although can't be caught");

            throw new RuntimeException("Payment method " + payment.getPaymentMethod()
                    + " is not available in your country");
        }
    }

}
