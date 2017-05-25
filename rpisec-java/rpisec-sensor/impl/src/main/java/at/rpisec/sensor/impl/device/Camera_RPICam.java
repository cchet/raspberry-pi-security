package at.rpisec.sensor.impl.device;

import at.rpisec.sensor.api.device.DeviceSettings;
import at.rpisec.sensor.api.device.camera.CameraDevice;
import at.rpisec.sensor.api.listener.CameraDeviceListener;
import at.rpisec.sensor.impl.listener.ImageData;
import at.rpisec.sensor.impl.listener.ImageEvent;
import at.rpisec.sensor.impl.util.DeviceSettingsUitl;

import java.io.*;
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
    private static final String DEVICE_PROPERTY_FILE = "device_camera";
    private static final String APP_PROPERTY_FILE = "app";
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

    public Camera_RPICam() {

        init();

        cameraReady = checkDeviceCamera();
    }

    private void init() {
        DeviceSettings settings = new DeviceSettingsUitl();
        Map<String, String> device_settings = settings.readSettings(DEVICE_PROPERTY_FILE);
        Map<String, String> app_settings = settings.readSettings(APP_PROPERTY_FILE);

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
    }

    private boolean checkDeviceCamera() {
        Process process;
        try {

            if (cameraTestApp.isEmpty() || cameraTestAppParam.isEmpty()) {
                System.out.println(CAMERA_TEST_APP_NOT_DEFINED_MESSAGE);
                return false;
            }

            Path testAppPath = Paths.get(cameraTestApp);
            if (!Files.exists(testAppPath, LinkOption.NOFOLLOW_LINKS)) {
                System.out.println(String.format(APP_NOT_FOUND_MESSAGE, cameraTestApp));
                return false;
            }

            if (cameraApp.isEmpty()) {
                System.out.println(CAMERA_APP_NOT_DEFINED_MESSAGE);
                return false;
            }

            Path cameraAppPath = Paths.get(cameraApp);
            if (!Files.exists(cameraAppPath, LinkOption.NOFOLLOW_LINKS)) {
                System.out.println(String.format(APP_NOT_FOUND_MESSAGE, cameraAppPath));
                return false;
            }

            String[] testCmdArgs = new String[]{cameraTestApp, cameraTestAppParam};
            Map<String, String> args = new HashMap<>();

            process = new ProcessBuilder(testCmdArgs).start();

            try (InputStream is = process.getInputStream()) {
                try (InputStreamReader isr = new InputStreamReader(is)) {
                    try (BufferedReader br = new BufferedReader(isr)) {
                        String line;

                        while ((line = br.readLine()) != null) {
                            List<String> parsedLineArgs = Arrays.asList(line.split(" "));
                            for (String arg : parsedLineArgs) {
                                if (arg.contains("=")) {
                                    String[] split_arg = arg.split("=");
                                    if (split_arg.length > 1) {
                                        args.put(split_arg[0], split_arg[1]);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (args.containsKey("supported")) {
                setSupported(args.get("supported").equals("1"));
            }

            if (args.containsKey("detected")) {
                setDetected(args.get("detected").equals("1"));
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    @Override
    public void addCameraDeviceListener(CameraDeviceListener listener) {
        if (listener == null)
            throw new NullPointerException("[Camera_RPICam] CameraDeviceListener is null.");

        synchronized (syncLock) {
            if (!listeners.contains(listener))
                listeners.add(listener);
        }
    }

    @Override
    public void removeCameraDeviceListener(CameraDeviceListener listener) {
        if (listener == null)
            throw new NullPointerException("[Camera_RPICam] CameraDeviceListener is null.");

        synchronized (syncLock) {
            listeners.remove(listener);
        }
    }

    private void fireNewImageEvent(ImageData imageData) {
        if (imageData == null)
            throw new NullPointerException("[fireNewImageEvent] imageData object is null.");

        synchronized (syncLock) {
            ImageEvent imageEvent = new ImageEvent(this, imageData);

            List<CameraDeviceListener> tmpListeners = (List<CameraDeviceListener>) ((ArrayList<CameraDeviceListener>) this.listeners).clone();

            for (CameraDeviceListener listener : tmpListeners)
                listener.onImageReceived(imageEvent);
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
            Process process;
            try {
                LocalDateTime today = LocalDateTime.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(IMAGE_FILE_PATTERN);
                String fileName = today.format(formatter) + IMAGE_FILE_SUFFIX;
                String fileNameWithPath = outputFilePath + fileName;

                commandLineArgs.add("-o");
                commandLineArgs.add(fileNameWithPath);

                if (DEBUG_MODE)
                    System.out.println("Params: [" + String.join(", ", commandLineArgs) + "]");

                process = new ProcessBuilder(commandLineArgs.toArray(new String[0])).start();

                if (DEBUG_MODE) {
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                System.out.println(line);
                            }
                        }catch (Exception e) {

                        }
                }

                ImageData imageData = new ImageData();
                imageData.setImageFilePath(outputFilePath);
                imageData.setFileName(fileName);
                imageData.setImageFlipped(commandLineArgs.contains("-hf") && commandLineArgs.contains("-vf"));

                fireNewImageEvent(imageData);

                if (DEBUG_MODE)
                    System.out.println("Image saved to '" + fileNameWithPath + "'.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stopDevice() {
        throw new UnsupportedOperationException("The camera does not need to be stopped");
    }
}
