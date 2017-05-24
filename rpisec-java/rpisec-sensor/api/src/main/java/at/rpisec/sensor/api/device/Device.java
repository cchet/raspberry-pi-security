package at.rpisec.sensor.api.device;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public interface Device {
    boolean isSupported();

    void setSupported(boolean supported);

    boolean isDetected();

    void setDetected(boolean detected);

    void runDevice();

    void stopDevice();
}
