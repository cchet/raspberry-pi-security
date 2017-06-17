package at.rpisec.auth.config;

import at.rpisec.auth.config.adapter.WebMvcConfigurerAdapterImpl;
import at.rpisec.auth.config.other.ServletContextInitializerImpl;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * This class holds the producers for the web related ebans.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Configuration
public class WebConfiguration {

    @Bean
    ServletContextInitializer produceWebApplicationInitializer() {
        return new ServletContextInitializerImpl();
    }

    @Bean
    WebMvcConfigurerAdapter produceWebMvcConfigurationAdaptor() {
        return new WebMvcConfigurerAdapterImpl();
    }
}
