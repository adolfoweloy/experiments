package au.com.aeloy.httpserverexperiment.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(HeavyProcessController.ENDPOINT)
public class HeavyProcessController {

    public static final String ENDPOINT = "/rest/heavy-process";

    @PostMapping
    public ResponseEntity<Void> process(@RequestBody RequestConfig input) {
        try {
            Thread.sleep(input.getTimeToWaitInSeconds() * 1000);
            return ResponseEntity.ok().build();
        } catch (InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
