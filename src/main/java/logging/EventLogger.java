
package logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class EventLogger {

    private final String fileName = "eventlog.dat";

    public synchronized void logEvent(Event event) {

        File file = new File(fileName);
        boolean fileExists = file.exists() && file.length() > 0;

        try (
            FileOutputStream fileOutputStream =
                    new FileOutputStream(file, true);

            ObjectOutputStream objectOutputStream =
                    fileExists
                    ? new AppendableObjectOutputStream(fileOutputStream)
                    : new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(event);
            objectOutputStream.flush();

            System.out.println("[LOGGED] " + event);

        } catch (IOException e) {
            System.err.println(
                    "Error writing event: " + e.getMessage()
            );
        }
    }

    private static class AppendableObjectOutputStream
            extends ObjectOutputStream {

        public AppendableObjectOutputStream(OutputStream output)
                throws IOException {
            super(output);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}
