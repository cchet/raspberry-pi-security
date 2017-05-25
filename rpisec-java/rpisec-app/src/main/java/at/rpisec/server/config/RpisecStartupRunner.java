package at.rpisec.server.config;

import at.rpisec.sensor.api.ISensorApplication;
import at.rpisec.sensor.api.exception.SensorAppStartupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.TaskScheduler;

import java.util.Objects;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/25/17
 */
public class RpisecStartupRunner implements CommandLineRunner {

    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private ISensorApplication sensorApp;
    @Autowired
    private Logger log;

    private static final long SENSOR_APP_RESTART_MILLIS = 10000;

    public static final class SensorAppKeepAliveTask implements Runnable {

        private final ISensorApplication sensorApp;
        private static final Logger log = LoggerFactory.getLogger(SensorAppKeepAliveTask.class);

        public SensorAppKeepAliveTask(ISensorApplication sensorApp) {
            this.sensorApp = Objects.requireNonNull(sensorApp, "Sensor application instance must not be null");
        }

        @Override
        public void run() {
            if (!sensorApp.isRunning()) {
                try {
                    sensorApp.start();
                    log.info("Sensor application successfully started");
                } catch (Throwable e) {
                    log.error("Sensor application failed to start", e);
                }
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            sensorApp.start();
            log.info("Sensor application successfully started");
        } catch (SensorAppStartupException e) {
            log.error("Sensor application failed to start");
        }

        taskScheduler.scheduleAtFixedRate(new SensorAppKeepAliveTask(sensorApp), SENSOR_APP_RESTART_MILLIS);
    }
}
