package at.rpisec.sensor.api.config;

import java.io.File;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 05/27/17
 */
public interface ICameraDeviceConfiguration {

    String getTestApp();

    String getTestAppCommand();

    String getAppPath();

    String getOutputPath();

    Integer getWidth();

    Integer getHeight();

    Integer getQuality();

    Integer getTimeout();

    boolean isFlip();

    String getFileNameDateTimePattern();

    default void checkConfiguration() {
        isFlip();
        final int width = getWidth();
        final int height = getHeight();
        final int timeout = getTimeout();
        final int quality = getQuality();

        if (width <= 0) {
            throw new IllegalArgumentException("Device width must be > 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Device height must be > 0");
        }
        if (timeout <= 0) {
            throw new IllegalArgumentException("Timeout must be > 0");
        }
        if ((quality <= 0) || (quality > 100)) {
            throw new IllegalArgumentException("Quality must be 0 < quality <= 100");
        }
        if (getTestApp().isEmpty()) {
            throw new IllegalArgumentException("Test app path must be defined");
        }
        if (getAppPath().isEmpty()) {
            throw new IllegalArgumentException("App path must be defined");
        }
        if (getOutputPath().isEmpty()) {
            throw new IllegalArgumentException("Output path must be defined");
        }
        File directory = new File(getOutputPath());
        if (!directory.exists()) {
            throw new IllegalArgumentException("Output path does not point to a directory");
        }
    }
}
