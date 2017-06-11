package at.rpisec.app.config;

import at.rpisec.app.logic.api.IIncidentLogic;
import at.rpisec.sensor.api.ISensorApplication;
import at.rpisec.sensor.impl.config.PropertiesfileSensorApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.TaskScheduler;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

/**
 * The sartup runner for setting up the sensor application which interacts with the hardware device.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/25/17
 */
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private ISensorApplication sensorApp;
    @Autowired
    private IIncidentLogic incidentLogic;
    @Autowired
    private Logger log;
    @Autowired
    @Qualifier(SecurityConfiguration.INCIDENT_IMAGE_LOCATION)
    private String imageDir;

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

            // validate image dir
            if(!Files.isDirectory(Paths.get(imageDir))) {
                throw new IllegalStateException(String.format("Defined parameter 'imageDir' does not exist or is not a directory. imageDir=%s",imageDir));
            }

            // start sensor application
            sensorApp.start(new PropertiesfileSensorApplicationConfiguration());
            sensorApp.register(incidentLogic::logIncidentWithImageAsync);

            // create restart task
            final SensorAppKeepAliveTask task = new SensorAppKeepAliveTask(sensorApp);
            final ScheduledFuture future = taskScheduler.scheduleAtFixedRate(task, SENSOR_APP_RESTART_MILLIS);
            task.setFuture(future);

            log.debug("Ended command line runner");
        } catch (Throwable e) {
            log.debug("Command line runner failed", e);
        }
    }
}
