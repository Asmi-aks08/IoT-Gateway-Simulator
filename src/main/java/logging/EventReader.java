
package logging;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class EventReader {

    public static void main(String[] args) {

        String fileName = "eventlog.dat";

        try (ObjectInputStream input =
                new ObjectInputStream(
                        new FileInputStream(fileName))) {

            System.out.println("===== SAVED SENSOR EVENTS =====");

            int count = 0;

            while (true) {
                try {
                    Event event = (Event) input.readObject();
                    System.out.println(event);
                    count++;
                } catch (EOFException e) {
                    break;
                }
            }

            System.out.println("===============================");
            System.out.println("Total events read: " + count);

        } catch (FileNotFoundException e) {
            System.out.println(
                    "No event log found. Run the logger first."
            );
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(
                    "Error reading events: " + e.getMessage()
            );
        }
    }
}
