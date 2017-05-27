package at.rpisec.sensor.impl.device;

import at.rpisec.sensor.api.device.Device;
import at.rpisec.sensor.api.device.irsensor.IRSensorDevice;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public class IRSensor_HCSR501 implements IRSensorDevice {

    private final Object syncLock = new Object();
    private GpioController gpioController;
    // don't know if there is a way to check if hardware is connected to gpio pins
    private boolean supported = true;
    private boolean detected = true;

    private List<Device> devices;

    private static final Logger log = LoggerFactory.getLogger(IRSensor_HCSR501.class);

    public IRSensor_HCSR501() {
        init();
    }

    private void init() {
        log.debug("Initializing HCSR501 sensor");
        devices = new ArrayList<>();
        log.debug("Initialized HCSR501 sensor");
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
        log.debug("Starting HCSR501 sensor");

        // Already running
        if (gpioController != null) {
            log.debug("Starting HCSR501 sensor failed. Already started");
            return;
        }

        gpioController = GpioFactory.getInstance();
        final GpioPinDigitalInput sensor = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_15, "hcsr501");
        sensor.setShutdownOptions(true);

        // create and register gpio pin listener
        log.debug("Registering sensor listener");
        sensor.addListener((GpioPinListenerDigital) event -> {
            log.debug("HCSR501 sensor at pin '{}' changed state to '{}'", event.getPin(), event.getState());

            if (PinState.HIGH.equals(event.getState())) {
                synchronized (syncLock) {
                    for (Device device : devices) {
                        log.debug("Running attached device. {}", device);
                        device.runDevice();
                        log.debug("Ran attached device");
                    }
                }
            }
        });
        log.debug("Registered sensor listener");
        log.debug("Started HCSR501 sensor");
    }

    @Override
    public void stopDevice() {
        log.debug("Stopping HCSR501 sensor");
        if (gpioController != null) {
            gpioController.shutdown();
            gpioController = null;
            log.debug("Stopped HCSR501 sensor");
        } else {
            log.debug("Stopping HCSR501 sensor failed. already stopped");
        }
    }

    @Override
    public void attachDevice(Device device) {
        synchronized (syncLock) {
            if (!devices.contains(device)) {
                devices.add(device);
                log.debug("Added device. {}", device);
            }
        }
    }

    @Override
    public void detachDevice(Device device) {
        synchronized (syncLock) {
            devices.remove(device);
            log.debug("Removed device. {}", device);
        }
    }
}
