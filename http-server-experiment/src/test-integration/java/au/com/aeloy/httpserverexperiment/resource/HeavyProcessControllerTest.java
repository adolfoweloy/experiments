package au.com.aeloy.httpserverexperiment.resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * This integration test was created to run some experiments in order to understand
 * how tomcat behaves in face of requests that take too long to be processed.
 * This simply shows that by default, there is no timeout limit that defines how
 * long a server can spent running some process. By default, the client side is in charge
 * to define the timeout to define how long it will wait for a response.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HeavyProcessControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldSendARequestThatTakesOneMinuteToBeProcessedSuccessfully() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ResponseEntity<Void> response = restTemplate.postForEntity(
                HeavyProcessController.ENDPOINT,
                createHttpRequest(new RequestConfig(60, asList("item1", "item2"))),
                Void.class);
        stopWatch.stop();

        Double totalTimeSeconds = stopWatch.getTotalTimeSeconds();
        System.out.println(totalTimeSeconds);

        assertThat(totalTimeSeconds, is(closeTo(60.0, 0.9)));
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.OK)));
    }

    @Test
    public void shouldRejectRequestsIfProcessing200RequestsSimultaneously() {

        List<Future<ResponseEntity<Void>>> responses = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(500);

        for (int i = 0; i < 300; i++) {
            responses.add(executor.submit(() -> {
                ResponseEntity<Void> response = restTemplate.postForEntity(
                        HeavyProcessController.ENDPOINT,
                        createHttpRequest(new RequestConfig(5, asList("item1", "item2"))),
                        Void.class);
                return response;
            }));
        }

        int rejectedRequests = responses.stream()
                .filter((f) -> {
                    try {
                        ResponseEntity<Void> response = f.get();
                        return response.getStatusCode().isError();
                    } catch (InterruptedException | ExecutionException e) {
                        return true;
                    }
        }).collect(Collectors.toList()).size();

        Assert.assertThat(rejectedRequests, is(equalTo(0)));
    }

    private <T> HttpEntity<T> createHttpRequest(T request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return new HttpEntity<>(request, headers);
    }

}
