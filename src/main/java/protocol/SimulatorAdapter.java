package protocol;

import device.Device;

public class SimulatorAdapter<T> implements ProtocolAdapter<T> {

    @Override
public T fetchData(Device<T> device) {
    try {
        throw new RuntimeException("Simulated sensor failure!");
    } catch (RuntimeException e) {
        System.err.println("EXCEPTION CAUGHT: " + e.getMessage());
        throw e;
    }
}

    @Override
    public String getProtocolName() {
        return "SIMULATOR";
    }
}