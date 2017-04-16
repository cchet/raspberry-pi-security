package at.rpisec.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.http.MediaType;
import org.springframework.validation.Validator;


/**
 * This class configures the rest part of this application.
 * <p>
 * See: http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/15/17
 */
public class RestConfiguration extends RepositoryRestConfigurerAdapter {

    @Autowired
    @Qualifier("jsr303Validator")
    private Validator validator;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setBasePath("/rest");
        config.setDefaultMediaType(MediaType.APPLICATION_JSON);
        config.setRepositoryDetectionStrategy(RepositoryDetectionStrategy.RepositoryDetectionStrategies.ANNOTATED);
    }

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("beforeCreate", validator);
        validatingListener.addValidator("beforeSave", validator);
    }
}
