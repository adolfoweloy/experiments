package au.com.aeloy;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Simple service to abstract the call to the node web server running locally.
 */
public class HelloService {

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
            ResponseEntity<String> result = rest.getForEntity(
                    URI.create("http://localhost:3000"), String.class);
            System.out.println(result.getStatusCode().toString());

            if (!result.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("error trying to connect");
            }

            return result.getBody();
        } catch (Exception e) {
            System.out.println("not possible to connect");
            throw new RuntimeException("error trying to connect");
        }

    }

}
