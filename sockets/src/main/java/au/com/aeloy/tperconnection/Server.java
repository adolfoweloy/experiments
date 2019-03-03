package au.com.aeloy.tperconnection;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public static ServerSocketWrapper createServer() throws IOException {
        // creates a simple server that listens on port 6060 for text messages
        ServerSocket serverSocket = ServerSocketFactory.getDefault()
                .createServerSocket(6060);
        return new ServerSocketWrapper(serverSocket);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // start running the server in a separate thread
        ServerSocketWrapper server = Server.createServer();
        new Thread(server).start();
    }

}
