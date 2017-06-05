package at.rpisec.oauth.config;

import at.rpisec.oauth.config.other.ConfigProperties;
import at.rpisec.oauth.config.other.IntegrationTestStartupRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/05/17
 */
@Configuration
@Profile(ConfigProperties.SupportedProfiles.INTEGRATION_TEST)
public class IntegrationTestConfig {

    @Bean
    CommandLineRunner produceIntegrationTestStartupRunner() {
        return new IntegrationTestStartupRunner();
    }
}
