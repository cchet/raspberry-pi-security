package at.rpisec.sensor.impl.device;

import at.rpisec.sensor.api.device.DeviceSettings;
import at.rpisec.sensor.api.device.camera.CameraDevice;
import at.rpisec.sensor.api.listener.CameraDeviceListener;
import at.rpisec.sensor.impl.listener.ImageData;
import at.rpisec.sensor.impl.listener.ImageEvent;
import at.rpisec.sensor.impl.util.DeviceSettingsUitl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public class Camera_RPICam implements CameraDevice {

    private static final String IMAGE_FILE_PATTERN = "yyyyMMddHHmmssSSS";
    private static final String IMAGE_FILE_SUFFIX = ".jpeg";
    private static final String DEVICE_PROPERTY_FILE = "sensor-app-device-camera";
    private static final String APP_PROPERTY_FILE = "sensor-app";
    private static final String APP_NOT_FOUND_MESSAGE = "Application %s not found.";
    private static final String CAMERA_TEST_APP_NOT_DEFINED_MESSAGE = "Path to camera test application not set.";
    private static final String CAMERA_APP_NOT_DEFINED_MESSAGE = "Path to camera application not set.";
    private static boolean DEBUG_MODE = false;
    private final Object syncLock = new Object();
    private boolean supported = false;
    private boolean detected = false;
    private List<String> commandLineArgs = new ArrayList<>();
    private String outputFilePath = "";
    private String cameraTestApp = "";
    private String cameraTestAppParam = "";
    private String cameraApp = "";
    private List<CameraDeviceListener> listeners = new ArrayList<>();
    private boolean cameraReady = false;

    private static final Logger log = LoggerFactory.getLogger(Camera_RPICam.class);

    public Camera_RPICam() {
        init();

        cameraReady = checkDeviceCamera();
    }

    private void init() {
        log.debug("Initializing rpi camera device");

        DeviceSettings settings = new DeviceSettingsUitl();
        Map<String, String> device_settings = settings.readSettings(DEVICE_PROPERTY_FILE);
        log.debug("Loaded device settings:");
        log.debug("Settings: {}", device_settings.toString());

        Map<String, String> app_settings = settings.readSettings(APP_PROPERTY_FILE);
        log.debug("Loaded app settings:");
        log.debug("Settings: {}", app_settings.toString());

        for (String key : device_settings.keySet()) {

            String value = device_settings.get(key).trim();

            switch (key) {
                case "device_flip": {
                    if (value.equals("1")) {
                        commandLineArgs.add("-hf");
                        commandLineArgs.add("-vf");
                    }
                    break;
                }
                case "device_width": {
                    commandLineArgs.add("-w");
                    commandLineArgs.add(value);
                    break;
                }
                case "device_height": {
                    commandLineArgs.add("-h");
                    commandLineArgs.add(value);
                    break;
                }
                case "device_quality": {
                    commandLineArgs.add("-q");

                    int quality = Integer.parseInt(value);
                    if (quality < 0 || quality > 100)
                        quality = 75;

                    commandLineArgs.add(String.valueOf(quality));
                    break;
                }
                case "device_timeout": {
                    commandLineArgs.add("-t");

                    int timeout = Integer.parseInt(value);
                    if (timeout <= 1 || timeout > 5)
                        timeout = 1;

                    commandLineArgs.add(String.valueOf(timeout));
                    break;
                }
                case "device_verbose": {
                    if (value.equals("1"))
                        commandLineArgs.add("-v");
                    break;
                }
                case "device_output_path": {
                    outputFilePath = value.trim();

                    if (!outputFilePath.endsWith(File.separator))
                        outputFilePath += File.separator;

                    File directory = new File(outputFilePath);
                    if (!directory.exists())
                        directory.mkdirs();
                    break;
                }
                case "device_test_app": {
                    cameraTestApp = value;
                    break;
                }
                case "device_test_app_param": {
                    cameraTestAppParam = value;
                    break;
                }
            }
        }

        cameraApp = device_settings.get("device_path");
        commandLineArgs.add(0, cameraApp);

        for (String key : app_settings.keySet()) {
            String value = app_settings.get(key).trim();
            switch (key) {
                case "debug": {
                    DEBUG_MODE = value.equals("1");
                }
            }
        }

        log.debug("Initialized rpi camera device");
    }

    private boolean checkDeviceCamera() {
        log.debug("Checking rpi camera device");
        Process process;
        try {

            if (cameraTestApp.isEmpty() || cameraTestAppParam.isEmpty()) {
                log.error(CAMERA_TEST_APP_NOT_DEFINED_MESSAGE);
                return false;
            }

            Path testAppPath = Paths.get(cameraTestApp);
            if (!Files.exists(testAppPath, LinkOption.NOFOLLOW_LINKS)) {
                log.error(String.format(APP_NOT_FOUND_MESSAGE, cameraTestApp));
                return false;
            }

            if (cameraApp.isEmpty()) {
                log.error(CAMERA_APP_NOT_DEFINED_MESSAGE);
                return false;
            }

            Path cameraAppPath = Paths.get(cameraApp);
            if (!Files.exists(cameraAppPath, LinkOption.NOFOLLOW_LINKS)) {
                log.error(String.format(APP_NOT_FOUND_MESSAGE, cameraAppPath));
                return false;
            }

            String[] testCmdArgs = new String[]{cameraTestApp, cameraTestAppParam};
            Map<String, String> args = new HashMap<>();

            process = new ProcessBuilder(testCmdArgs).start();

            log.debug("Testing camera via process");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;

                log.debug("Reading process result:");
                while ((line = br.readLine()) != null) {
                    log.debug("raw line: {}", line);
                    List<String> parsedLineArgs = Arrays.asList(line.split(" "));
                    for (String arg : parsedLineArgs) {
                        if (arg.contains("=")) {
                            String[] split_arg = arg.split("=");
                            if (split_arg.length > 1) {
                                log.debug("{}={}", split_arg[0], split_arg[1]);
                                args.put(split_arg[0], split_arg[1]);
                            }
                        }
                    }
                }
                log.debug("Process arguments read");
            } catch (Exception e) {
                log.error("Process failed to read from input stream", e);
            }

            if (args.containsKey("supported")) {
                setSupported(args.get("supported").equals("1"));
            }

            if (args.containsKey("detected")) {
                setDetected(args.get("detected").equals("1"));
            }

        } catch (IOException e) {
            log.error("Checking rpi camera device failed", e);
            return false;
        }

        if (supported) {
            log.debug("Camera device check passed successfully");
        } else {
            log.error("Camera device check failed");
        }
        return true;
    }


    @Override
    public void addCameraDeviceListener(CameraDeviceListener listener) {
        Objects.requireNonNull(listener, "[Camera_RPICam] CameraDeviceListener must not be null.");

        synchronized (syncLock) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
                log.debug("Added camera device listener. {}", listener.toString());
            }
        }
    }

    @Override
    public void removeCameraDeviceListener(CameraDeviceListener listener) {
        Objects.requireNonNull(listener, "[Camera_RPICam] CameraDeviceListener must not be null.");

        synchronized (syncLock) {
            listeners.remove(listener);
            log.debug("Removed camera device listener. {}", listener.toString());
        }
    }

    private void fireNewImageEvent(ImageData imageData) {
        Objects.requireNonNull(imageData, "[fireNewImageEvent] imageData object is null.");

        synchronized (syncLock) {
            log.debug("Creating new image event");
            ImageEvent imageEvent = new ImageEvent(this, imageData);

            List<CameraDeviceListener> tmpListeners = (List<CameraDeviceListener>) ((ArrayList<CameraDeviceListener>) this.listeners).clone();

            for (CameraDeviceListener listener : tmpListeners) {
                log.debug("Notifying observer {}", listener.toString());
                listener.onImageReceived(imageEvent);
                log.debug("Notified observer {}", listener.toString());
            }
        }
    }

    @Override
    public boolean isSupported() {
        return this.supported;
    }

    @Override
    public void setSupported(boolean supported) {
        this.supported = supported;
    }

    @Override
    public boolean isDetected() {
        return this.detected;
    }

    @Override
    public void setDetected(boolean detected) {
        this.detected = detected;
    }

    @Override
    public void runDevice() {
        if (cameraReady) {
            log.debug("Start camera device");
            Process process;
            try {
                LocalDateTime today = LocalDateTime.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(IMAGE_FILE_PATTERN);
                String fileName = today.format(formatter) + IMAGE_FILE_SUFFIX;
                String fileNameWithPath = outputFilePath + fileName;

                commandLineArgs.add("-o");
                commandLineArgs.add(fileNameWithPath);

                if (DEBUG_MODE)
                    log.debug("Params: [" + String.join(", ", commandLineArgs) + "]");

                process = new ProcessBuilder(commandLineArgs.toArray(new String[0])).start();

                if (DEBUG_MODE) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            log.debug(line);
                        }
                    } catch (Exception e) {

                    }
                }

                ImageData imageData = new ImageData();
                imageData.setImageFilePath(outputFilePath);
                imageData.setFileName(fileName);
                imageData.setImageFlipped(commandLineArgs.contains("-hf") && commandLineArgs.contains("-vf"));

                fireNewImageEvent(imageData);

                if (DEBUG_MODE)
                    log.debug("Image saved to '" + fileNameWithPath + "'.");
            } catch (IOException e) {
                log.error("Could not run camera device", e);
            }
        } else {
            log.error("Camera device not ready");
        }
    }

    @Override
    public void stopDevice() {
        throw new UnsupportedOperationException("The camera does not need to be stopped");
    }
}
