package device;

import java.util.Random;

public class MotionSensor implements Device<Boolean> {

    private String id;
    private String name;
    private Random random = new Random();

    public MotionSensor(String id, String name) {
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
    public Boolean readData() {
        return random.nextBoolean();
    }
}