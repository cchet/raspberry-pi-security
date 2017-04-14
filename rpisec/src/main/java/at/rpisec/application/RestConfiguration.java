package at.rpisec.application;

import at.rpisec.rest.AliveRestService;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This class configures the rest part of this application.
 *
 * See: http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/15/17
 */
@Configuration
@EnableSwagger2
public class RestConfiguration {
    @Bean
    public Docket produceSwaggerDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(AliveRestService.class.getPackage().getName()))
                .build();
    }

    @Bean
    public ResourceConfig produceJerseyResourceConfig(){
        final ResourceConfig config = new ResourceConfig();
        config.register(AliveRestService.class);

        return config;
    }
}
