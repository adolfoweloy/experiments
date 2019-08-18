package au.com.aeloy.retrybenchmark.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/another")
public class AnotherEndpoint {
    @GetMapping
    public String get() {
        return "another one!";
    }
}
