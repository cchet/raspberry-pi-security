package at.rpisec.config;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/19/17
 */
public class ModelMapperConfigurer extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        super.configure(factory);

    }
}
