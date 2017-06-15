package at.rpisec.app.config;

import at.rpisec.app.config.other.ConfigProperties;
import at.rpisec.app.config.other.StartupRunner;
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
 * This class holds the producer for the prod profile.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/25/17
 */
@Configuration
@Profile(ConfigProperties.SupportedProfiles.PROD)
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
