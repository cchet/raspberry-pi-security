package at.rpisec.oauth.config;

import at.rpisec.oauth.config.other.ConfigProperties;
import at.rpisec.oauth.config.other.StartupRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/05/17
 */
@Profile(ConfigProperties.SupportedProfiles.DEV)
public class DevConfig {

    @Bean
    CommandLineRunner produceStartupRunner() {
        return new StartupRunner();
    }
}
