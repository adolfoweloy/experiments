package au.com.aeloy.micrometerexperiment.controller;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simple")
public class SimpleController {

    private final Counter counter;

    public SimpleController(MeterRegistry registry) {
        this.counter = registry.counter("metric.hello");
    }

    @Timed(value = "metric.hello.time", histogram = true)
    @GetMapping
    public String hello() {
        counter.increment();
        return "hello!";
    }

}
