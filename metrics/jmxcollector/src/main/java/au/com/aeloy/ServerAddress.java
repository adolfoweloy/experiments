package au.com.aeloy;

public class ServerAddress {
    private final String hostName;
    private final int port;

    public ServerAddress(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }
}
