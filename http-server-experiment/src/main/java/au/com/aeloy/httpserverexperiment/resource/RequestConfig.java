package au.com.aeloy.httpserverexperiment.resource;

import java.util.List;

/**
 * RequestConfig provides data to be sent to controllers in order to run
 * experiments around Tomcat (which is the web application server being used).
 */
public class RequestConfig {
    private int timeToWaitInSeconds;
    private List<String> content;

    public RequestConfig() {}

    public RequestConfig(int timeToWaitInSeconds, List<String> content) {
        this.timeToWaitInSeconds = timeToWaitInSeconds;
        this.content = content;
    }

    public int getTimeToWaitInSeconds() {
        return timeToWaitInSeconds;
    }

    public void setTimeToWaitInSeconds(int timeToWaitInSeconds) {
        this.timeToWaitInSeconds = timeToWaitInSeconds;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
