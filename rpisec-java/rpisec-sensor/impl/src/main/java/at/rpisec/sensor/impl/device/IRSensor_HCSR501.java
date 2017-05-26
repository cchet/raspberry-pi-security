package at.rpisec.sensor.impl.device;

import at.rpisec.sensor.api.device.Device;
import at.rpisec.sensor.api.device.DeviceSettings;
import at.rpisec.sensor.api.device.irsensor.IRSensorDevice;
import at.rpisec.sensor.impl.util.DeviceSettingsUitl;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public class IRSensor_HCSR501 implements IRSensorDevice {

    private static final String APP_PROPERTY_FILE = "sensor-app";
    private static boolean DEBUG_MODE = false;
    private final Object syncLock = new Object();
    private Thread gpioThread = null;
    private GpioEventLoop gpioEventLoop = null;

    // don't know if there is a way to check if hardware is connected to gpio pins
    private boolean supported = true;
    private boolean detected = true;

    private List<Device> devices;

    public IRSensor_HCSR501() {
        init();
    }

    private void init() {
        devices = new ArrayList<>();

        DeviceSettings settings = new DeviceSettingsUitl();
        Map<String, String> app_settings = settings.readSettings(APP_PROPERTY_FILE);

        for (String key : app_settings.keySet()) {
            String value = app_settings.get(key).trim();
            switch (key) {
                case "debug": {
                    DEBUG_MODE = value.equals("1");
                }
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
        if (gpioThread != null) {
            if (gpioThread.isAlive())
                return;
        }

        gpioEventLoop = new GpioEventLoop();
        gpioThread = new Thread(gpioEventLoop);
        gpioThread.start();

        try {
            gpioThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Device thread interrupted", e);
        }
    }

    @Override
    public void stopDevice() {
        if (gpioThread != null && gpioThread.isAlive()) {
            if (gpioEventLoop != null) {
                gpioEventLoop.stopEventLoop();

                gpioEventLoop = null;
                gpioThread = null;
            }
        }
    }

    @Override
    public void attachDevice(Device device) {
        synchronized (syncLock) {
            if (!devices.contains(device))
                devices.add(device);
        }
    }

    @Override
    public void detachDevice(Device device) {
        synchronized (syncLock) {
            devices.remove(device);
        }
    }

    private final class GpioEventLoop implements Runnable {

        private final Object syncLock = new Object();
        private GpioController gpio = null;
        private GpioPinDigitalInput sensore = null;
        private boolean secureWait = true;

        private void stopEventLoop() {

            getSensore().removeAllListeners();
            getGpio().shutdown();

            synchronized (syncLock) {
                syncLock.notify();
                setSecureWait(false);
            }
        }

        @Override
        public void run() {
            gpio = GpioFactory.getInstance();
            sensore = gpio.provisionDigitalInputPin(RaspiPin.GPIO_15, "hcsr501");
            getSensore().setShutdownOptions(true);

            // create and register gpio pin listener
            getSensore().addListener((GpioPinListenerDigital) event -> {
                // display pin state on console

                if (DEBUG_MODE)
                    System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());

                if (event.getState() == PinState.HIGH) {
                    synchronized (syncLock) {
                        List<Device> tmpDevices = (List<Device>) ((ArrayList<Device>) devices).clone();

                        for (Device device : tmpDevices)
                            device.runDevice();
                    }
                }
            });

            synchronized (syncLock) {
                try {
                    if (isSecureWait())
                        syncLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private GpioController getGpio() {
            return gpio;
        }

        private GpioPinDigitalInput getSensore() {
            return sensore;
        }

        public boolean isSecureWait() {
            return secureWait;
        }

        public void setSecureWait(boolean secureWait) {
            this.secureWait = secureWait;
        }
    }
}
