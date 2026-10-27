package device;

public interface Device<T> {

    String getId();

    String getName();

    T readData();
}