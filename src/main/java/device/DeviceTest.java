package device;

public class DeviceTest {

    public static void main(String[] args) {

        TemperatureSensor temperatureSensor =
                new TemperatureSensor("TEMP001", "Temperature Sensor");

        MotionSensor motionSensor =
                new MotionSensor("MOTION001", "Motion Sensor");

        System.out.println("Device: " + temperatureSensor.getName());
        System.out.println("ID: " + temperatureSensor.getId());
        System.out.println("Temperature: " + temperatureSensor.readData() + " °C");

        System.out.println();

        System.out.println("Device: " + motionSensor.getName());
        System.out.println("ID: " + motionSensor.getId());
        System.out.println("Motion detected: " + motionSensor.readData());
    }
}