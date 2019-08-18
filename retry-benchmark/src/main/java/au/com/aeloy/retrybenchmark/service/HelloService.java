package au.com.aeloy.retrybenchmark.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Simple service to abstract the call to the node web server running locally.
 */
@Component
public class HelloService {

    private static final Logger log = LoggerFactory.getLogger(HelloService.class);

    /**
     * This simple method, just tries to send a request to a simple node
     * server created within scripts directory.
     *
     * @return return the services response.
     */
    public String request() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(2000);
        RestTemplate rest = new RestTemplate(clientHttpRequestFactory);
        try {
            log.info("iniciando a requisição");
            ResponseEntity<String> result = rest.getForEntity(
                    URI.create("http://localhost:3000"), String.class);

            if (!result.getStatusCode().is2xxSuccessful()) {
                log.info("requisição falou");
                throw new RuntimeException("error trying to connect");
            }

            log.info("requisição finalizada com sucesso");

            return result.getBody();
        } catch (Exception e) {
            log.error("Not possible to connect");
            throw new RuntimeException("error trying to connect");
        }

    }

}
