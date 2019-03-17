package au.com.aeloy;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class EmbeddedTomcatEx {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();

        tomcat.setPort(8082);
        tomcat.getConnector().setProperty("maxThreads", "2");
        tomcat.getConnector().setProperty("acceptCount", "2");

        Context ctx = tomcat.addContext("/", new File(".").getAbsolutePath());

        Tomcat.addServlet(ctx, "Embedded", new RegularServlet());
        Tomcat.addServlet(ctx, "Async", new AsyncServlet()).setAsyncSupported(true);

        ctx.addServletMapping("/*", "Embedded");
        ctx.addServletMapping("/async", "Async");

        tomcat.start();
        tomcat.getServer().await();
    }
}
