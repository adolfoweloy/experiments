package au.com.aeloy.tperconnection;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connection extends Observable implements Runnable {
    private final Socket socket;
    private final String name;

    public Connection(String name, Socket socket, Observer observer) {
        this.name = name;
        this.socket = socket;
        addObserver(observer);
    }

    @Override
    public void run() {
        Pattern sleepCommandPattern = Pattern.compile("sleep:(\\d+)");

        try {
            byte[] payload = new byte[1024];
            InputStream stream = socket.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(stream);
            int bytesRead;
            while ((bytesRead = bis.read(payload)) != -1) {
                String command = new String(Arrays.copyOf(payload, bytesRead)).trim();

                // if a command to sleep was sent, then sleep for the amount of time requested
                // this allows me to think about timeouts and the amount of threads that can run at the same time
                Matcher matcher = sleepCommandPattern.matcher(command);
                if (matcher.matches()) {
                    String param = matcher.group(1);
                    long timeToSleep = Long.parseLong(param);
                    Thread.sleep(timeToSleep);
                }

                System.out.println("[" + name + "] received payload: " + command);

                if (command.contains("quit")) {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeConnectionAndNotifyTheServer();
        }
    }

    private void closeConnectionAndNotifyTheServer() {
        try {
            socket.close();
            setChanged();
            notifyObservers(name);

            System.out.println("[" + name + "] connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
