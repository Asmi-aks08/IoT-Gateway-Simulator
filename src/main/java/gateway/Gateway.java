
package gateway;

import device.Device;
import protocol.ProtocolAdapter;
import logging.Event;
import logging.EventLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Gateway {

    private final List<Device<?>> devices = new ArrayList<>();
    private final List<ProtocolAdapter<?>> adapters = new ArrayList<>();

    private final List<RegisteredDevice<?>> registeredDevices =
            new ArrayList<>();

    private final ExecutorService executorService =
            Executors.newFixedThreadPool(2);

    private final EventLogger eventLogger = new EventLogger();

    // Register a device
    public void registerDevice(Device<?> device) {
        devices.add(device);
    }

    // Register a protocol adapter
    public void registerAdapter(ProtocolAdapter<?> adapter) {
        adapters.add(adapter);
    }

    // Register a device and its matching adapter together
    public <T> void registerDevice(
            Device<T> device,
            ProtocolAdapter<T> adapter) {

        registeredDevices.add(
                new RegisteredDevice<>(device, adapter)
        );

        registerDevice(device);
        registerAdapter(adapter);
    }

    // Poll a device using its supplied adapter
    public <T> Future<T> pollDevice(
            Device<T> device,
            ProtocolAdapter<T> adapter) {

        return executorService.submit(() -> {

            T data = adapter.fetchData(device);

            System.out.println(
                    "[" + adapter.getProtocolName() + "] "
                    + device.getName()
                    + " = " + data
                    + " | Thread: "
                    + Thread.currentThread().getName()
            );

            Event event = new Event(
                    device.getId(),
                    device.getName(),
                    adapter.getProtocolName(),
                    String.valueOf(data)
            );

            eventLogger.logEvent(event);

            return data;
        });
    }

    // Poll every registered device using its matching adapter
    public List<Future<?>> pollRegisteredDevices() {

        List<Future<?>> results = new ArrayList<>();

        for (RegisteredDevice<?> registered : registeredDevices) {
            results.add(pollRegisteredDevice(registered));
        }

        return results;
    }

    private <T> Future<T> pollRegisteredDevice(
            RegisteredDevice<T> registered) {

        return pollDevice(
                registered.device,
                registered.adapter
        );
    }

    // Stop the thread pool
    public void shutdown() {
        executorService.shutdown();
    }

    // Keeps each device paired with the correct adapter
    private static class RegisteredDevice<T> {

        private final Device<T> device;
        private final ProtocolAdapter<T> adapter;

        private RegisteredDevice(
                Device<T> device,
                ProtocolAdapter<T> adapter) {

            this.device = device;
            this.adapter = adapter;
        }
    }
}
