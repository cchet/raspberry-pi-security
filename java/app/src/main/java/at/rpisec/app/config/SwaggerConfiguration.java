package at.rpisec.app.config;

import at.rpisec.app.config.other.ConfigProperties;
import at.rpisec.app.rest.controller.ClientRestController;
import at.rpisec.app.rest.controller.InternalRestController;
import at.rpisec.shared.rest.constants.AppRestConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/09/17
 */
@Configuration
@Profile(ConfigProperties.SupportedProfiles.DEV)
public class SwaggerConfiguration {

    @Bean
    Docket produceClientApiSwaggerDocket() {
        return createBasicAuthRestApiSwaggerDocket(ClientRestController.class.getSimpleName(), AppRestConstants.CLIENT_REST_API_BASE + "/**");
    }

    @Bean
    Docket produceInternalApiSwaggerDocket() {
        return createBasicAuthRestApiSwaggerDocket(InternalRestController.class.getSimpleName(), AppRestConstants.INTERNAL_REST_API_BASE + "/**");
    }

    private Docket createBasicAuthRestApiSwaggerDocket(final String groupName,
                                                       final String antPath) {
        final String securityKey = "basicAuth";
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .securitySchemes(Collections.singletonList(new BasicAuth(securityKey)))
                .securityContexts(Collections.singletonList(createSecurityContext(securityKey, antPath)))
                .select()
                .paths(PathSelectors.ant(antPath))
                .build();
    }

    private SecurityContext createSecurityContext(final String schemeName,
                                                  final String antPath) {
        return SecurityContext.builder()
                              .securityReferences(Collections.singletonList(new SecurityReference(schemeName, new AuthorizationScope[0])))
                              .forPaths(PathSelectors.ant(antPath))
                              .build();
    }
}
