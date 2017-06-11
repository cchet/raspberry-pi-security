package at.rpisec.app.config;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

/**
 * This class configures the {@link ma.glasnost.orika.MapperFacade} for usage with our models.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
public class ConfigurableMapperImpl extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {

    }
}
