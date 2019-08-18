package au.com.aeloy.retrybenchmark.api;

import au.com.aeloy.retrybenchmark.service.HelloService;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/retry-template")
public class RetryTemplateController {
    private final HelloService helloService;
    private final RetryTemplate retryTemplate;

    public RetryTemplateController(
            HelloService helloService,
            RetryTemplate retryTemplate
    ) {
        this.helloService = helloService;
        this.retryTemplate = retryTemplate;
    }

    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok(
                retryTemplate.execute(context -> helloService.request()));
    }

}
