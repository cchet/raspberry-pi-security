package at.rpisec.sensor.impl.config;

import at.rpisec.sensor.api.config.ICameraDeviceConfiguration;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 05/27/17
 */
public class PropertiesFileCameraDeviceConfiguration implements ICameraDeviceConfiguration {

    private final Properties properties;

    public static final String PROPERTIES_FILE_NAME = "sensor-app-camera-device.properties";

    public PropertiesFileCameraDeviceConfiguration() throws IOException {
        properties = new Properties();
        properties.load(PropertiesFileCameraDeviceConfiguration.class.getResourceAsStream("/" + PROPERTIES_FILE_NAME));
        checkConfiguration();
    }

    @Override
    public String getTestApp() {
        return properties.getProperty("test_app", "");
    }

    @Override
    public String getTestAppCommand() {
        return properties.getProperty("test_app_command", "");
    }

    @Override
    public String getAppPath() {
        return properties.getProperty("app_path", "");
    }

    @Override
    public String getOutputPath() {
        return properties.getProperty("output_path", "");
    }

    @Override
    public Integer getWidth() {
        return Integer.valueOf(properties.getProperty("width"));
    }

    @Override
    public Integer getHeight() {
        return Integer.valueOf(properties.getProperty("height"));
    }

    @Override
    public Integer getQuality() {
        return Integer.valueOf(properties.getProperty("quality"));
    }

    @Override
    public Integer getTimeout() {
        return Integer.valueOf(properties.getProperty("timeout"));
    }

    @Override
    public boolean isFlip() {
        return properties.containsKey("flip") ? Boolean.valueOf(properties.getProperty("flip")) : false;
    }

    @Override
    public String getFileNameDateTimePattern() {
        return properties.getProperty("pattern", "yyyy-MM-dd_hh:mm:ss.SSSS");
    }
}
