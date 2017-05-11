package at.rpisec.oauth.config;

import at.rpisec.oauth.config.other.StartupRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/11/17
 */
@Configuration
@Profile("prod")
public class ConfigurationProd {

    @Bean
    CommandLineRunner produceStartupRunner() {
        return new StartupRunner(true);
    }
}
