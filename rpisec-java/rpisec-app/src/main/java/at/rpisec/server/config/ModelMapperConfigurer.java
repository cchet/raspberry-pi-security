package at.rpisec.server.config;

import at.rpisec.server.jpa.model.User;
import at.rpisec.server.shared.rest.model.UserDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/19/17
 */
public class ModelMapperConfigurer extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(User.class, UserDto.class).byDefault().register();
    }
}
