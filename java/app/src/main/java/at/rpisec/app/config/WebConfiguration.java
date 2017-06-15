package at.rpisec.app.config;

import at.rpisec.app.config.adaptor.WebMvcConfigurerAdapterImpl;
import at.rpisec.app.config.other.ServletContextInitializerImpl;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * This class holds the producers for the web related beans.
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
