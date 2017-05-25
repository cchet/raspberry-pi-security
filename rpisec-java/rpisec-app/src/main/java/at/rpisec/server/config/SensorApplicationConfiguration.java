package at.rpisec.server.config;

import at.rpisec.sensor.api.ISensorApplication;
import at.rpisec.sensor.impl.SensorApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/25/17
 */
@Configuration
public class SensorApplicationConfiguration {

    @Bean
    ISensorApplication produceSensorApplication() {
        return new SensorApplication();
    }
}
