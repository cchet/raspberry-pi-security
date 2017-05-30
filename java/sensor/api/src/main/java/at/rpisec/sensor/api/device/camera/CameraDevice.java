package at.rpisec.sensor.api.device.camera;

import at.rpisec.sensor.api.device.Device;
import at.rpisec.sensor.api.listener.CameraDeviceListener;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public interface CameraDevice extends Device {
    void addCameraDeviceListener(CameraDeviceListener listener);

    void removeCameraDeviceListener(CameraDeviceListener listener);
}
