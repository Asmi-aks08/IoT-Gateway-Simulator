package logging;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    private String deviceId;
    private String deviceName;
    private String protocol;
    private String data;
    private LocalDateTime timestamp;

    public Event(String deviceId,
                 String deviceName,
                 String protocol,
                 String data) {

        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.protocol = protocol;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] "
                + "Device: " + deviceName
                + " | ID: " + deviceId
                + " | Protocol: " + protocol
                + " | Data: " + data;
    }
}