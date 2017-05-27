package at.rpisec.server.config;

import at.rpisec.sensor.api.ISensorApplication;
import at.rpisec.sensor.api.exception.SensorAppShutdownException;
import at.rpisec.sensor.impl.SensorApplication;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/25/17
 */
@Configuration
@Profile("prod")
public class ProdConfiguration {

    @Bean
    ISensorApplication produceSensorApplication() {
        return new SensorApplication();
    }

    @Bean
    CommandLineRunner produceStartupRunner() {
        return new StartupRunner();
    }

    @Bean
    ApplicationListener<ContextStoppedEvent> produceApplicationListener(final ISensorApplication sensorApp,
                                                                        final Logger log) {
        return (evt) -> {
            log.info("Cleaning up");
            if ((sensorApp != null) && (sensorApp.isRunning())) {
                try {
                    sensorApp.stop();
                } catch (SensorAppShutdownException e) {
                    log.error("Cleaning up failed", e);
                }
            }
            log.info("Cleanup up");
        };
    }
}
