package au.com.aeloy.micrometerexperiment.controller;

import com.google.common.collect.ImmutableMap;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * This controller is just an example to illustrate how to read metrics when using Spring Boot 2.
 * Instead of having the /actuator/metrics endpoint, given the complexity of the data available,
 * when using SimpleMeterRegistry (driver used by default), you should create your own way to
 * serialize your metrics collecting just what make sense for your application.
 */
@RestController
@RequestMapping("/metrics")
public class SimpleMeterRegistryController {

    private final MeterRegistry meterRegistry;

    public SimpleMeterRegistryController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping
    @ResponseBody
    public Map<String, String> get() {
        return ImmutableMap.of(
            "simple.hello.count", Double.toString(meterRegistry.find("simple.hello").counter().count()));
    }
}
