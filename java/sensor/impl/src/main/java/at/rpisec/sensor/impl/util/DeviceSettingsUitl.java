package at.rpisec.sensor.impl.util;

import at.rpisec.sensor.api.device.DeviceSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public class DeviceSettingsUitl implements DeviceSettings {
    @Override
    public Map<String, String> readSettings(String settingsFile) {
        Properties properties = new Properties();
        Map<String, String> mappedProperties = new HashMap<>();

        try {
            InputStream propReader = getClass().getResourceAsStream("/" + settingsFile + ".properties");
            if (propReader == null)
                throw new NullPointerException("Properties file '" + settingsFile + ".properties' not found.");
            else
                properties.load(propReader);

            propReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String key : properties.stringPropertyNames()) {
            mappedProperties.put(key, properties.getProperty(key));
        }

        return mappedProperties;
    }
}
