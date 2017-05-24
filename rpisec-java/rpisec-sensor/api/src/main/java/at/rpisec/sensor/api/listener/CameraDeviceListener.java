package at.rpisec.sensor.api.listener;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public interface CameraDeviceListener {
    void onImageReceived(IncidentEvent event);
}
