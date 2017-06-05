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
 * Resource server configuration for the oauth protected resources.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/11/17
 */
public class ResourceServerConfigurerAdapterImpl extends ResourceServerConfigurerAdapter {

    @Autowired
    private ConfigProperties.OauthProperties oauthProperties;
    @Autowired
    private ResourceServerTokenServices tokenServices;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(oauthProperties.getResourceId())
                 .tokenServices(tokenServices)
                 .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            // Exclude for oauth authentication for web resources
            .antMatchers("/bootstrap/**").permitAll()
            .antMatchers("/jquery/**").permitAll()
            .antMatchers("/css/**").permitAll()
            .antMatchers("/js/**").permitAll()
            // Exclude for oauth authentication for auth server hosted api
            .antMatchers(String.format("%s%s**", ClientRestConstants.BASE_URI, ClientRestConstants.REL_URI_REGISTER)).permitAll()
            .and().authorizeRequests()
            // Exclude for oauth authentication for auth server hosted api
            .antMatchers(String.format("%s%s**", ClientRestConstants.BASE_URI, ClientRestConstants.REL_URI_UNREGISTER)).permitAll()
            .and().authorizeRequests()
            // Exclude for oauth authentication for alive api
            .antMatchers("/api/system/alive").permitAll()
            .and().authorizeRequests()
            // All other resources protected by oauth
            .antMatchers("/api/**")
            .access(String.format("#oauth2.hasAnyScope('%s','%s') and hasRole('%s')", SecurityConstants.SCOPE_READ, SecurityConstants.SCOPE_WRITE, SecurityConstants.CLIENT))
            .and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
