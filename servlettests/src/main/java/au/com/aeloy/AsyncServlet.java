package au.com.aeloy;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AsyncServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {

        AsyncContext asyncContext = req.startAsync(req, resp);

        new Thread(() -> {

            try {
                HttpServletResponse asyncResponse = (HttpServletResponse) asyncContext.getResponse();
                HttpServletRequest asyncRequest = (HttpServletRequest) asyncContext.getRequest();

                asyncResponse.setContentType("text/plain");
                String sleep = asyncRequest.getParameter("sleep");

                Thread.sleep(Integer.valueOf(sleep) * 1000);

                PrintWriter writer = asyncResponse.getWriter();
                writer.println("async finished processing");

                asyncContext.complete();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

}
