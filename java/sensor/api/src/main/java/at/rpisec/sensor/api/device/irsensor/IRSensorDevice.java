package at.rpisec.sensor.api.device.irsensor;

import at.rpisec.sensor.api.device.Device;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public interface IRSensorDevice extends Device {
    void attachDevice(Device device);

    void detachDevice(Device device);
}
