package au.com.aeloy;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class RegularServlet extends HttpServlet {

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

}
