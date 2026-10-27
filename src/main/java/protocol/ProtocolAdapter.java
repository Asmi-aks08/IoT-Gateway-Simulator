package protocol;

import device.Device;

public interface ProtocolAdapter<T> {

    T fetchData(Device<T> device);

    String getProtocolName();
}