
package dashboard;

import device.MotionSensor;
import device.TemperatureSensor;
import gateway.Gateway;
import protocol.SimulatorAdapter;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Future;

public class Dashboard extends Application {

    private Gateway gateway;

    private TemperatureSensor temperatureSensor;
    private MotionSensor motionSensor;

    private SimulatorAdapter<Double> temperatureAdapter;
    private SimulatorAdapter<Boolean> motionAdapter;

    private final ObservableList<String> eventHistory =
            FXCollections.observableArrayList();

    private final DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void start(Stage stage) {

        // 1. Create Gateway
        gateway = new Gateway();

        // 2. Create sensors
        temperatureSensor = new TemperatureSensor(
                "TEMP001",
                "Temperature Sensor"
        );

        motionSensor = new MotionSensor(
                "MOTION001",
                "Motion Sensor"
        );

        // 3. Create protocol adapters
        temperatureAdapter = new SimulatorAdapter<>();
        motionAdapter = new SimulatorAdapter<>();

        // 4. Register devices and adapters
        gateway.registerDevice(temperatureSensor);
        gateway.registerDevice(motionSensor);

        gateway.registerAdapter(temperatureAdapter);
        gateway.registerAdapter(motionAdapter);

        // 5. Create dashboard labels
        Label title = new Label("IoT Gateway Simulator");
        Label status = new Label("Gateway Status: RUNNING");

        Label temperatureLabel =
                new Label("Temperature Sensor: Waiting...");

        Label motionLabel =
                new Label("Motion Sensor: Waiting...");

        Label historyTitle = new Label("Recent Sensor Events");

        // 6. Display recent events
        ListView<String> historyList =
                new ListView<>(eventHistory);

        historyList.setPrefHeight(250);

        // 7. Clear visible history button
        Button clearHistoryButton =
                new Button("Clear Visible History");

        clearHistoryButton.setOnAction(event -> {

            Alert confirmation = new Alert(
                    Alert.AlertType.CONFIRMATION
            );

            confirmation.setTitle("Clear Visible History");
            confirmation.setHeaderText("Clear dashboard history?");
            confirmation.setContentText(
                    "This clears the visible list only. "
                    + "Saved events in eventlog.dat will remain."
            );

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    eventHistory.clear();
                }
            });
        });

        // 8. Arrange dashboard layout
        VBox layout = new VBox(
                12,
                title,
                status,
                temperatureLabel,
                motionLabel,
                historyTitle,
                historyList,
                clearHistoryButton
        );

        layout.setStyle("-fx-padding: 15;");

        Scene scene = new Scene(layout, 550, 450);

        stage.setTitle("IoT Gateway Dashboard");
        stage.setScene(scene);
        stage.show();

        // 9. Poll sensors every 2 seconds
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(2),
                        event -> {

                            try {
                                Future<Double> temperatureFuture =
                                        gateway.pollDevice(
                                                temperatureSensor,
                                                temperatureAdapter
                                        );

                                Future<Boolean> motionFuture =
                                        gateway.pollDevice(
                                                motionSensor,
                                                motionAdapter
                                        );

                                Double temperature =
                                        temperatureFuture.get();

                                Boolean motion =
                                        motionFuture.get();

                                // Update current readings
                                temperatureLabel.setText(
                                        "Temperature Sensor: "
                                        + String.format(
                                                "%.2f",
                                                temperature
                                        )
                                        + " °C"
                                );

                                motionLabel.setText(
                                        "Motion Sensor: " + motion
                                );

                                // Add events to visible history
                                String time = LocalTime.now()
                                        .format(timeFormatter);

                                eventHistory.add(
                                        time
                                        + " | Temperature: "
                                        + String.format(
                                                "%.2f",
                                                temperature
                                        )
                                        + " °C"
                                );

                                eventHistory.add(
                                        time + " | Motion: " + motion
                                );

                                // Keep only the latest 20 visible events
                                while (eventHistory.size() > 20) {
                                    eventHistory.remove(0);
                                }

                                // Scroll to the newest event
                                if (!eventHistory.isEmpty()) {
                                    historyList.scrollTo(
                                            eventHistory.size() - 1
                                    );
                                }

                            } catch (Exception e) {

                                temperatureLabel.setText(
                                        "Temperature Sensor: Error"
                                );

                                motionLabel.setText(
                                        "Motion Sensor: Error"
                                );

                                e.printStackTrace();
                            }
                        }
                )
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // 10. Stop the timeline and gateway when closing
        stage.setOnCloseRequest(event -> {
            timeline.stop();
            gateway.shutdown();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}