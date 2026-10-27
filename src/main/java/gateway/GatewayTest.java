package gateway;

import device.MotionSensor;
import device.TemperatureSensor;
import protocol.SimulatorAdapter;

public class GatewayTest {

    public static void main(String[] args) {

        Gateway gateway = new Gateway();

        TemperatureSensor temperatureSensor =
                new TemperatureSensor(
                        "TEMP001",
                        "Temperature Sensor"
                );

        MotionSensor motionSensor =
                new MotionSensor(
                        "MOTION001",
                        "Motion Sensor"
                );

        SimulatorAdapter<Double> temperatureAdapter =
                new SimulatorAdapter<>();

        SimulatorAdapter<Boolean> motionAdapter =
                new SimulatorAdapter<>();

        gateway.registerDevice(temperatureSensor);
        gateway.registerDevice(motionSensor);

        gateway.registerAdapter(temperatureAdapter);
        gateway.registerAdapter(motionAdapter);

        gateway.pollDevice(
                temperatureSensor,
                temperatureAdapter
        );

        gateway.pollDevice(
                motionSensor,
                motionAdapter
        );

        gateway.shutdown();
    }
}