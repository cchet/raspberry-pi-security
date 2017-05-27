package at.rpisec.sensor.impl;

import at.rpisec.sensor.api.ISensorApplication;
import at.rpisec.sensor.api.IncidentImageObserver;
import at.rpisec.sensor.api.config.ISensorApplicationConfiguration;
import at.rpisec.sensor.api.device.camera.CameraDevice;
import at.rpisec.sensor.api.device.irsensor.IRSensorDevice;
import at.rpisec.sensor.api.exception.SensorAppShutdownException;
import at.rpisec.sensor.api.exception.SensorAppStartupException;
import at.rpisec.sensor.impl.device.Camera_RPICam;
import at.rpisec.sensor.impl.device.IRSensor_HCSR501;
import at.rpisec.sensor.impl.listener.ImageData;
import at.rpisec.sensor.impl.listener.ImageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */
public class SensorApplication implements ISensorApplication {

    private ISensorApplicationConfiguration applicationConfig;
    private IRSensorDevice sensor = null;
    private List<IncidentImageObserver> observers = new LinkedList<>();
    private final Object syncLock = new Object();
    private boolean running = false;

    private static final String IMAGE_EXTENSION = "jpeg";
    private static final Logger log = LoggerFactory.getLogger(SensorApplication.class);

    @Override
    public void register(IncidentImageObserver observer) {
        Objects.requireNonNull(observer, "Observer must not be null");
        synchronized (syncLock) {
            log.debug("Registering incident image observer instance: {}", observer.toString());
            if (!observers.contains(observer)) {
                observers.add(observer);
                log.debug("Registered incident image observer instance");
            } else {
                log.debug("Registering of incident image observer instance not possible. Already registered");
            }
        }
    }

    @Override
    public void remove(IncidentImageObserver observer) {
        Objects.requireNonNull(observer, "Null observer cannot be null");
        synchronized (syncLock) {
            log.debug("Unregistering incident image observer instance: {}", observer.toString());
            final boolean removed = observers.remove(observer);
            log.debug("Unregistered incident image observer instance: removed={}", removed);
        }
    }

    @Override
    public void start(final ISensorApplicationConfiguration applicationConfig) throws SensorAppStartupException {
        this.applicationConfig = Objects.requireNonNull(applicationConfig, "Application configuration must not be null");

        log.info("Starting sensor application");
        try {
            CameraDevice camera = new Camera_RPICam(applicationConfig.getCameraDeviceConfiguration());

            if (!camera.isSupported()) {
                throw new IllegalStateException("CameraDevice not supported");
            }

            if (!camera.isDetected()) {
                throw new IllegalStateException("No cameraDevice detected");
            }

            sensor = new IRSensor_HCSR501();
            sensor.attachDevice(camera);
            log.debug("Attached camera device to HCSR501 sensor");

            // CameraDeviceListener
            log.debug("Registering camera device listener");
            camera.addCameraDeviceListener(event -> {
                log.debug("Calling camera device listener");
                Objects.requireNonNull(event, "[CameraDeviceListener] event object is null");
                ImageData imageData = Objects.requireNonNull(((ImageEvent) event).getImageData(), "[CameraDeviceListener] event object is null");

                byte[] imageBytes = null;
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    final File imageFile = new File(imageData.getImageFilePath().concat(imageData.getFileName()));
                    if (imageFile.exists()) {
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        ImageIO.write(bufferedImage, IMAGE_EXTENSION, baos);
                        imageBytes = baos.toByteArray();
                    }
                } catch (IOException e) {
                    log.error("Calling camera device listener failed", e);
                }

                if (imageBytes != null) {
                    synchronized (syncLock) {
                        for (IncidentImageObserver listener : observers) {
                            log.debug("Notifying camera attached observer. {}", listener);
                            listener.handle(imageBytes, IMAGE_EXTENSION);
                            log.debug("Notified camera attached observer. {}", listener);
                        }
                    }
                } else {
                    log.debug("No image data provided by new image event");
                }
                log.debug("Called camera device listener");
            });
            log.debug("Registered camera device listener");

            sensor.runDevice();
            running = true;
            log.info("Started sensor application");
        } catch (Exception e) {
            log.info("Starting of sensor application failed");
            throw new SensorAppStartupException("Startup of sensor application failed", e);
        }
    }

    @Override
    public void shutdown() throws SensorAppShutdownException {
        log.info("Stopping sensor application");
        applicationConfig = null;
        try {
            observers.clear();
            if (sensor != null) {
                sensor.stopDevice();
            }
            log.info("Stopped sensor application");
        } catch (Exception e) {
            log.info("Stopping of sensor application failed");
            throw new SensorAppShutdownException("Sensor application failed to shutdown", e);
        } finally {
            running = false;
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override public ISensorApplicationConfiguration getCurrentConfiguration() {
        return applicationConfig;
    }
}
