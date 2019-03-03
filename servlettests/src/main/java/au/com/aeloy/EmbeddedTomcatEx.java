package au.com.aeloy;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

public class EmbeddedTomcatEx {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8082);

        Context ctx = tomcat.addContext("/", new File(".").getAbsolutePath());

        Tomcat.addServlet(ctx, "Embedded", new HttpServlet() {
            @Override
            protected void service(HttpServletRequest req, HttpServletResponse resp)
                    throws IOException {

                String response = "";
                String sleep = req.getParameter("sleep");
                if (sleep != null) {
                    try {
                        Thread.sleep(Integer.valueOf(sleep) * 1000);
                        response = "success";
                    } catch (InterruptedException e) {
                        response = "failed";
                        e.printStackTrace();
                    }
                }
                Writer w = resp.getWriter();
                w.write("Embedded Tomcat servlet.\n");
                w.write(response);
                w.flush();
                w.close();
            }
        });

        ctx.addServletMapping("/*", "Embedded");

        tomcat.start();
        tomcat.getServer().await();
    }
}
