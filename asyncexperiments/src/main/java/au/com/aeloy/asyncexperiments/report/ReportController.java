package au.com.aeloy.asyncexperiments.report;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
@RequestMapping("report")
public class ReportController {

    @PostMapping
    public Callable<String> requestReport() {
        return () -> {
            Thread.sleep(3000);
            return "Hello after sleeping 3 seconds";
        };
    }

}
