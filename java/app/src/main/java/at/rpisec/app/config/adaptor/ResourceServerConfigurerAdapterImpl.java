package at.rpisec.app.config.adaptor;

import at.rpisec.app.config.other.ConfigProperties;
import at.rpisec.shared.rest.constants.AppRestConstants;
import at.rpisec.shared.rest.constants.SecurityConstants;
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
        // Match any request on /api which are protected by oauth
        http.requestMatchers()
            .antMatchers(AppRestConstants.CLIENT_REST_API_BASE + "/**")
            .and().authorizeRequests()
            // All requests under /api are protected by basic auth
            .anyRequest()
            .access(String.format("#oauth2.hasAnyScope('%s','%s') and hasRole('%s')", SecurityConstants.SCOPE_READ, SecurityConstants.SCOPE_WRITE, SecurityConstants.CLIENT))
            // We have rest so need for cross site request forgery protection
            .and().csrf().disable()
            // rest api has to be called stateless
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
