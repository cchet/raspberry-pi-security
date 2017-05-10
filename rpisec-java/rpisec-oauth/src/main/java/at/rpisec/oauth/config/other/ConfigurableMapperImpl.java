package at.rpisec.oauth.config.other;

import at.rpisec.oauth.jpa.model.User;
import at.rpisec.server.shared.rest.model.UserDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * This class configures the {@link ma.glasnost.orika.MapperFacade} for usage with our models.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
public class ConfigurableMapperImpl extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(User.class, UserDetails.class).byDefault().register();
    }
}
