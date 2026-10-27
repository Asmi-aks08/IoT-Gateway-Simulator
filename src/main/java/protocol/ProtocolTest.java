package protocol;

import device.MotionSensor;
import device.TemperatureSensor;

public class ProtocolTest {

    public static void main(String[] args) {

        TemperatureSensor temperatureSensor =
                new TemperatureSensor("TEMP001", "Temperature Sensor");

        MotionSensor motionSensor =
                new MotionSensor("MOTION001", "Motion Sensor");

        SimulatorAdapter<Double> temperatureAdapter =
                new SimulatorAdapter<>();

        SimulatorAdapter<Boolean> motionAdapter =
                new SimulatorAdapter<>();

        Double temperature =
                temperatureAdapter.fetchData(temperatureSensor);

        Boolean motion =
                motionAdapter.fetchData(motionSensor);

        System.out.println("Protocol: "
                + temperatureAdapter.getProtocolName());

        System.out.println("Temperature: "
                + temperature + " °C");

        System.out.println();

        System.out.println("Protocol: "
                + motionAdapter.getProtocolName());

        System.out.println("Motion detected: "
                + motion);
    }
}