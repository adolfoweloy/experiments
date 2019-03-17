package au.com.aeloy.asyncexperiments.payment;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static au.com.aeloy.asyncexperiments.payment.PaymentMethod.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Locale.forLanguageTag;

@Repository
class AvailablePaymentMethods {

    private static final Map<Locale, List<PaymentMethod>> paymentMethodByLocale = ImmutableMap.of(
            Locale.ENGLISH, asList(CREDIT_CARD),
            forLanguageTag("en-AU"), asList(CREDIT_CARD, BPAY),
            forLanguageTag("pt-BR"), asList(CREDIT_CARD, BOLETO)
    );

    List<PaymentMethod> paymentMethodFor(Locale locale) {
        return paymentMethodByLocale.getOrDefault(locale, emptyList());
    }

}
