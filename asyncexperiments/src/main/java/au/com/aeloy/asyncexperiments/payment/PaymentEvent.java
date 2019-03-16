package au.com.aeloy.asyncexperiments.payment;

import org.springframework.context.ApplicationEvent;

public class PaymentEvent extends ApplicationEvent {
    private final String status;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    PaymentEvent(Payment source, String status) {
        super(source);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Payment getPayment() {
        return (Payment) source;
    }
}
