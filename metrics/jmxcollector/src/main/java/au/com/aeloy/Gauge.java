package au.com.aeloy;

public class Gauge {
    private final String key;
    private final long value;

    public Gauge(String key, long value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public long getValue() {
        return value;
    }
}
