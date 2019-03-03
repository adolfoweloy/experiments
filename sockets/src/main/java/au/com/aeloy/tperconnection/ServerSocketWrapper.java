package au.com.aeloy.tperconnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerSocketWrapper implements Observer, Runnable {
    private final ServerSocket server;
    private final AtomicInteger sequence = new AtomicInteger(1);
    private final Map<String, Connection> connections = new HashMap<>();

    public ServerSocketWrapper(ServerSocket server) {
        this.server = server;
    }

    @Override
    public void update(Observable observable, Object connectionName) {
        connections.remove(connectionName);
        System.out.println("[" + connectionName + "] connection removed");
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = server.accept();
                String connectionName = "conn-" + sequence.getAndIncrement();

                Connection connection = new Connection(connectionName, socket, this);
                connections.put(connectionName, connection);

                System.out.println("[" + connectionName + "] connection created");

                new Thread(connection).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
