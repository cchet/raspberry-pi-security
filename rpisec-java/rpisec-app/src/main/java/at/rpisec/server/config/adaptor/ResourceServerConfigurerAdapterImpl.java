package at.rpisec.server.config.adaptor;

import at.rpisec.server.config.ConfigProperties;
import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.constants.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/11/17
 */
public class ResourceServerConfigurerAdapterImpl extends ResourceServerConfigurerAdapter {

    @Autowired
    private ConfigProperties.RpisecProperties rpisecProperties;
    @Autowired
    private ResourceServerTokenServices tokenServices;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(rpisecProperties.getResourceId())
                 .tokenServices(tokenServices)
                 .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(String.format("%s/**", ClientRestConstants.BASE_URI))
            .access(String.format("#oauth2.clientHasRole('%s') and #oauth2.isClient() and #oauth2.hasScope('read')", SecurityConstants.ROLE_CLIENT))
            .and()
            .csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
