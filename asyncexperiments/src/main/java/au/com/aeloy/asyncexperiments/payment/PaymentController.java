package au.com.aeloy.asyncexperiments.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final Payments payments;
    private final ApplicationEventPublisher applicationEventPublisher;

    public PaymentController(
            Payments payments,
            ApplicationEventPublisher applicationEventPublisher) {
        this.payments = payments;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Payment payment) {
        logger.debug(payment.toString());

        try {
            payments.send(payment);
        } catch (Exception e) {
            logger.error(
                    "This wont be executed since void return type wont " +
                    "propagate the exception to the calling thread");
            return ResponseEntity.badRequest().build();
        }

        logger.debug("finished calling the async service");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/process")
    public ResponseEntity<Void> process(@RequestBody Payment payment) {
        // OMG, all these logs are terrible. They are good but make my code so ugly :'(
        logger.debug("Going to process the payment {}", payment.toString());

        payments.process(payment)
            .thenAccept((status) -> applicationEventPublisher.publishEvent(new PaymentEvent(payment, status)));

        logger.debug("I don't want to wait the result to be done");

        return ResponseEntity.ok().build();
    }

}
