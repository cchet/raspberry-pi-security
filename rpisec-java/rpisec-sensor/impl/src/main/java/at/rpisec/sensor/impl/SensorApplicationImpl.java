package at.rpisec.sensor.impl;

import at.rpisec.sensor.api.IncidentImageReportListener;
import at.rpisec.sensor.api.SensorApplication;
import at.rpisec.sensor.api.device.camera.CameraDevice;
import at.rpisec.sensor.api.device.irsensor.IRSensorDevice;
import at.rpisec.sensor.impl.device.Camera_RPICam;
import at.rpisec.sensor.impl.device.IRSensor_HCSR501;
import at.rpisec.sensor.impl.listener.ImageData;
import at.rpisec.sensor.impl.listener.ImageEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */
public class SensorApplicationImpl implements SensorApplication {

    private IRSensorDevice sensor = null;
    private List<IncidentImageReportListener> listeners = new ArrayList<>();
    private final Object syncLock = new Object();

    @Override
    public void register(IncidentImageReportListener listener) {
        synchronized (syncLock) {
            if (!listeners.contains(listener))
                listeners.add(listener);
        }
    }

    @Override
    public void remove(IncidentImageReportListener listener) {
        synchronized (syncLock) {
            listeners.remove(listener);
        }
    }

    @Override
    public void start() {
        CameraDevice camera = new Camera_RPICam();

        if (!camera.isSupported()) {
            System.out.println("CameraDevice is not supported!");
            return;
        }

        if (!camera.isDetected()) {
            System.out.println("CameraDevice is not detected!");
            return;
        }

        sensor = new IRSensor_HCSR501();
        sensor.attachDevice(camera);

        // CameraDeviceListener
        camera.addCameraDeviceListener(event -> {
            if (event == null)
                throw new NullPointerException("[CameraDeviceListener] event object is null.");

            ImageData imageData = ((ImageEvent) event).getImageData();
            if (imageData == null)
                throw new NullPointerException("[CameraDeviceListener] imageData object is null.");

            byte[] imageBytes = null;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                File imageFile = new File(new StringBuilder().append(imageData.getImageFilePath()).append(imageData.getFileName()).toString());
                if (imageFile.exists()) {
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    ImageIO.write(bufferedImage, "jpeg", baos);
                    imageBytes = baos.toByteArray();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            synchronized (syncLock) {
                if (imageBytes != null) {
                    for (IncidentImageReportListener listener : listeners)
                        listener.handle(imageBytes);
                }
            }
        });

        sensor.runDevice();
    }

    @Override
    public void shutdown() {
        if (sensor != null)
            sensor.stopDevice();
    }
}
