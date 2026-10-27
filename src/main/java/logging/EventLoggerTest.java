
package logging;

public class EventLoggerTest {

    public static void main(String[] args) {

        EventLogger logger = new EventLogger();

        Event temperatureEvent = new Event(
                "TEMP001",
                "Temperature Sensor",
                "SIMULATOR",
                "27.76 °C"
        );

        Event motionEvent = new Event(
                "MOTION001",
                "Motion Sensor",
                "SIMULATOR",
                "true"
        );

        logger.logEvent(temperatureEvent);
        logger.logEvent(motionEvent);

        System.out.println("Two events submitted for logging.");
    }
}
