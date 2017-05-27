package at.rpisec.sensor.impl.device;

import at.rpisec.sensor.api.config.ICameraDeviceConfiguration;
import at.rpisec.sensor.api.device.camera.CameraDevice;
import at.rpisec.sensor.api.listener.CameraDeviceListener;
import at.rpisec.sensor.impl.listener.ImageData;
import at.rpisec.sensor.impl.listener.ImageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public class Camera_RPICam implements CameraDevice {

    private final ICameraDeviceConfiguration cameraDeviceConfig;
    private final Object syncLock = new Object();
    private final DateTimeFormatter formatter;

    private boolean cameraReady = false;
    private boolean supported = false;
    private boolean detected = false;
    private List<String> commandLineArgs = new ArrayList<>();
    private List<CameraDeviceListener> listeners = new ArrayList<>();

    private static final String IMAGE_FILE_SUFFIX = ".jpeg";
    private static final Logger log = LoggerFactory.getLogger(Camera_RPICam.class);

    public Camera_RPICam(final ICameraDeviceConfiguration cameraDeviceConfig) {
        this.cameraDeviceConfig = Objects.requireNonNull(cameraDeviceConfig, "Camera device configuration must not be null");
        formatter = DateTimeFormatter.ofPattern(cameraDeviceConfig.getFileNameDateTimePattern());

        init();

        cameraReady = checkDeviceCamera();
    }

    private void init() {
        log.debug("Initializing rpi camera device");

        commandLineArgs.add(cameraDeviceConfig.getAppPath());
        if (cameraDeviceConfig.isFlip()) {
            commandLineArgs.add("-hf");
            commandLineArgs.add("-vf");
        }
        commandLineArgs.add("-w");
        commandLineArgs.add(cameraDeviceConfig.getWidth().toString());
        commandLineArgs.add("-h");
        commandLineArgs.add(cameraDeviceConfig.getHeight().toString());
        commandLineArgs.add("-q");
        commandLineArgs.add(cameraDeviceConfig.getQuality().toString());
        commandLineArgs.add("-t");
        commandLineArgs.add(cameraDeviceConfig.getTimeout().toString());
        if (log.isDebugEnabled()) {
            commandLineArgs.add("-v");
        }

        log.debug("Initialized rpi camera device");
    }

    private boolean checkDeviceCamera() {
        log.debug("Checking rpi camera device");
        Process process;
        try {
            String[] testCmdArgs = new String[]{cameraDeviceConfig.getTestApp(), cameraDeviceConfig.getTestAppCommand()};
            Map<String, String> args = new HashMap<>();

            process = new ProcessBuilder(testCmdArgs).start();

            log.debug("Testing camera via process");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;

                log.debug("Reading process result");
                while ((line = br.readLine()) != null) {
                    log.debug("raw result line : {}", line);
                    List<String> parsedLineArgs = Arrays.asList(line.split(" "));
                    for (String arg : parsedLineArgs) {
                        if (arg.contains("=")) {
                            String[] split_arg = arg.split("=");
                            if (split_arg.length > 1) {
                                log.debug("result: {}={}", split_arg[0], split_arg[1]);
                                args.put(split_arg[0], split_arg[1]);
                            }
                        }
                    }
                }
                log.debug("Read process result");
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
        } else {
            log.debug("Checking rpi camera device failed");
            return false;
        }

        log.debug("Checked rpi camera device successfully");
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
            log.debug("Firing image event. {} observers", listeners.size());
            final ImageEvent imageEvent = new ImageEvent(this, imageData);

            for (CameraDeviceListener listener : listeners) {
                log.debug("Notifying observer {}", listener.toString());
                listener.onImageReceived(imageEvent);
                log.debug("Notified observer {}", listener.toString());
            }
            log.debug("Fired image event");
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
        log.debug("Running camera device");
        if (cameraReady) {
            Process process;
            try {
                LocalDateTime today = LocalDateTime.now();

                String fileName = today.format(formatter) + IMAGE_FILE_SUFFIX;
                String fileNameWithPath = cameraDeviceConfig.getOutputPath() + fileName;

                commandLineArgs.add("-o");
                commandLineArgs.add(fileNameWithPath);

                log.debug("Params: [" + String.join(", ", commandLineArgs) + "]");

                process = new ProcessBuilder(commandLineArgs.toArray(new String[0])).start();

                if (log.isDebugEnabled()) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            log.debug(line);
                        }
                    }
                }

                ImageData imageData = new ImageData();
                imageData.setImageFilePath(cameraDeviceConfig.getOutputPath());
                imageData.setFileName(fileName);
                imageData.setImageFlipped(commandLineArgs.contains("-hf") && commandLineArgs.contains("-vf"));

                fireNewImageEvent(imageData);

                log.debug("Ran camera device");
            } catch (Exception e) {
                log.debug("Running camera device failed", e);
            }
        } else {
            log.debug("Running camera device failed. Camera not ready");
        }
    }

    @Override
    public void stopDevice() {
        throw new UnsupportedOperationException("The camera does not need to be stopped");
    }
}
