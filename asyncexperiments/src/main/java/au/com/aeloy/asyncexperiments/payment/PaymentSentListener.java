package au.com.aeloy.asyncexperiments.payment;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentSentListener {

    private static final Logger logger = LoggerFactory.getLogger(PaymentSentListener.class);

    @EventListener
    public void onSent(PaymentEvent event) {
        RestTemplate rest = new RestTemplate();

        ResponseEntity<Void> response = rest.postForEntity(
                "https://enir00k02s13.x.pipedream.net/",
                new PaymentResponse(event.getPayment(), event.getStatus()),
                Void.class, ImmutableMap.of());

        logger.info("Callback for payment {} with status \"{}\" sent. Http status: {}",
                event.getPayment(), event.getStatus(), response.getStatusCode().value());
    }

    @Data
    private static class PaymentResponse {
        private final Payment payment;
        private final String status;
    }

}
