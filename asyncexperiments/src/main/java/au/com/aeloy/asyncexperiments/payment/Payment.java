package au.com.aeloy.asyncexperiments.payment;

import lombok.Data;

import java.util.Date;
import java.util.Locale;

@Data
public class Payment {
    private PaymentMethod paymentMethod;
    private int amountInCents;
    private Date dueDate;
    private Locale locale;
}
