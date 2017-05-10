package at.rpisec.oauth.config;

import at.rpisec.oauth.config.adapter.WebMvcConfigurerAdapterImpl;
import at.rpisec.oauth.config.other.ServletContextInitializerImpl;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
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
