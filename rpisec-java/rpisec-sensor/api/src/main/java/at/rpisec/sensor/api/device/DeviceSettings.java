package at.rpisec.sensor.api.device;

import java.util.Map;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public interface DeviceSettings {
    Map<String, String> readSettings(String settingsFile);
}
