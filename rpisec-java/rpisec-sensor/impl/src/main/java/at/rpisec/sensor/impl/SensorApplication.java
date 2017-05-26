package at.rpisec.sensor.impl;

import at.rpisec.sensor.api.ISensorApplication;
import at.rpisec.sensor.api.IncidentImageObserver;
import at.rpisec.sensor.api.device.camera.CameraDevice;
import at.rpisec.sensor.api.device.irsensor.IRSensorDevice;
import at.rpisec.sensor.api.exception.SensorAppShutdownException;
import at.rpisec.sensor.api.exception.SensorAppStartupException;
import at.rpisec.sensor.impl.device.Camera_RPICam;
import at.rpisec.sensor.impl.device.IRSensor_HCSR501;
import at.rpisec.sensor.impl.listener.ImageData;
import at.rpisec.sensor.impl.listener.ImageEvent;
import com.pi4j.wiringpi.GpioUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */
public class SensorApplication implements ISensorApplication {

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
            if (!observers.contains(observer)) {
                observers.add(observer);
                log.info("Added incident image observer instance: {}", observer.toString());
            }
        }
    }

    @Override
    public void remove(IncidentImageObserver observer) {
        Objects.requireNonNull(observer, "Null observer cannot be null");
        synchronized (syncLock) {
            observers.remove(observer);
            log.info("Removed incident image observer instance: {}", observer.toString());
        }
    }

    @Override
    public void start() throws SensorAppStartupException {
        log.info("Starting sensor application");
        try {
            CameraDevice camera = new Camera_RPICam();

            if (!camera.isSupported()) {
                throw new IllegalStateException("CameraDevice not supported");
            }

            if (!camera.isDetected()) {
                throw new IllegalStateException("No cameraDevice detected");
            }

            sensor = new IRSensor_HCSR501();
            sensor.attachDevice(camera);

            // CameraDeviceListener
            camera.addCameraDeviceListener(event -> {
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
                    throw new IllegalStateException("Could not read sensor image data", e);
                }

                synchronized (syncLock) {
                    if (imageBytes != null) {
                        for (IncidentImageObserver listener : observers) {
                            listener.handle(imageBytes, IMAGE_EXTENSION);
                        }
                    }
                }
            });

            sensor.runDevice();
            running = true;
            log.info("Sensor application successfully started");
        } catch (Exception e) {
            log.info("Sensor application failed to start");
            throw new SensorAppStartupException("Startup of sensor application failed", e);
        }
    }

    @Override
    public void shutdown() throws SensorAppShutdownException {
        log.info("Shtudown sensor application");
        try {
            observers.clear();
            if (sensor != null) {
                sensor.stopDevice();
            }
            log.info("Shtudown sensor application successfully");
        } catch (Exception e) {
            log.info("Shtudown sensor application failed to shutdown gracefully");
            throw new SensorAppShutdownException("Sensor application failed to shutdown", e);
        } finally {
            running = false;
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
