package at.rpisec.sensor.impl.config;

import at.rpisec.sensor.api.config.ICameraDeviceConfiguration;
import at.rpisec.sensor.api.config.ISensorApplicationConfiguration;

import java.io.IOException;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 05/27/17
 */
public class PropertiesfileSensorApplicationConfiguration implements ISensorApplicationConfiguration {

    private final ICameraDeviceConfiguration cameraDeviceConfiguration;

    public PropertiesfileSensorApplicationConfiguration() throws IOException {
        cameraDeviceConfiguration = new PropertiesFileCameraDeviceConfiguration();
    }

    @Override
    public ICameraDeviceConfiguration getCameraDeviceConfiguration() {
        return cameraDeviceConfiguration;
    }
}
