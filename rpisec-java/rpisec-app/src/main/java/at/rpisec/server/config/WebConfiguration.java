package at.rpisec.server.config;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

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

    @Bean
    Docket produceSwaggerDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/rpisec")
                .select()
                .paths(PathSelectors.ant("/rest/**"))
                .build();
    }
}
