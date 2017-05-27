package at.rpisec.server.config;

import at.rpisec.sensor.api.ISensorApplication;
import at.rpisec.sensor.impl.config.PropertiesfileSensorApplicationConfiguration;
import at.rpisec.server.logic.api.IIncidentLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.TaskScheduler;

import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

/**
 * The sartup runner for setting up the sensor application which interacts with the hardware device.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/25/17
 */
public class SensorApplicationStartupRunner implements CommandLineRunner {

    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private ISensorApplication sensorApp;
    @Autowired
    private IIncidentLogic incidentLogic;
    @Autowired
    private Logger log;

    private static final int MAX_RESTART_COUNT = 10;
    private static final long SENSOR_APP_RESTART_MILLIS = 10000;

    public static final class SensorAppKeepAliveTask implements Runnable {

        private final ISensorApplication sensorApp;
        private int count = 0;
        private ScheduledFuture future;
        private static final Logger log = LoggerFactory.getLogger(SensorAppKeepAliveTask.class);

        public SensorAppKeepAliveTask(ISensorApplication sensorApp) {
            this.sensorApp = Objects.requireNonNull(sensorApp, "Sensor application instance must not be null");
        }

        @Override
        public void run() {
            if (!sensorApp.isRunning()) {
                try {
                    sensorApp.start(sensorApp.getCurrentConfiguration());
                    count = 0;
                    log.info("Sensor application successfully started");
                } catch (Throwable e) {
                    count++;
                    if (MAX_RESTART_COUNT == count) {
                        future.cancel(false);
                        log.error("Could not restart the sensor application for {} times, therefore giving up", MAX_RESTART_COUNT);
                    } else {
                        log.error("Sensor application failed to start", e);
                    }
                }
            }
        }

        public void setFuture(ScheduledFuture future) {
            this.future = future;
        }
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            log.debug("Started command line runner");
            sensorApp.start(new PropertiesfileSensorApplicationConfiguration());
            sensorApp.register(incidentLogic::logIncidentWithImageAsync);

            final SensorAppKeepAliveTask task = new SensorAppKeepAliveTask(sensorApp);
            final ScheduledFuture future = taskScheduler.scheduleAtFixedRate(task, SENSOR_APP_RESTART_MILLIS);
            task.setFuture(future);
            log.debug("Ended command line runner");
        } catch (Throwable e) {
            log.debug("Command line runner failed", e);
        }
    }
}
