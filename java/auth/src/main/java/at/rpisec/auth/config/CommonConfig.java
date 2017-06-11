package at.rpisec.auth.config;

import at.rpisec.auth.config.other.ConfigProperties;
import at.rpisec.auth.config.other.StartupRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/05/17
 */
@Configuration
@Profile({ConfigProperties.SupportedProfiles.DEV, ConfigProperties.SupportedProfiles.PROD})
public class CommonConfig {

    @Bean
    CommandLineRunner produceStartupRunner() {
        return new StartupRunner();
    }
}
