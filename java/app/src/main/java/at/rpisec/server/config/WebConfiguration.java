package at.rpisec.server.config;

import at.rpisec.server.config.adaptor.WebMvcConfigurerAdapterImpl;
import at.rpisec.server.rest.ClientRestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

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
