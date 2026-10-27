package device;

import java.util.Random;

public class TemperatureSensor implements Device<Double> {

    private String id;
    private String name;
    private Random random = new Random();

    public TemperatureSensor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Double readData() {
        return 20.0 + (40.0 - 20.0) * random.nextDouble();
    }
}