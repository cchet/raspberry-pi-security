package at.rpisec.server.config.adaptor;

import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.constants.SecurityConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Configuration for protection of the for the authentication hosted rest api which is not protected by oauth.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/11/17
 */
public class WebSecurityConfigurerAdapterImpl extends WebSecurityConfigurerAdapter {

    @Value("${security.user.name}")
    private String systemUser;
    @Value("${security.user.password}")
    private String systemPassword;

    /**
     * Create the system user in memory.
     *
     * @param auth the authentication manager builder we configure in memory authentication
     * @throws Exception if configuration fails for any reason
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser(systemUser)
            .password(systemPassword)
            .roles(SecurityConstants.SYSTEM);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Match any request on /internal which are protected by basic http auth
        http.requestMatchers().antMatchers(ClientRestConstants.BASE_INTERNAL_URI + "/**")
            // enable basic auth for all resources under /internal
            .and().httpBasic()
            // any request to api under /internal must be authenticated against system user
            .and().authorizeRequests().anyRequest().hasAnyRole(SecurityConstants.SYSTEM)
            // We have rest so need for cross site request forgery protection
            .and().csrf().disable()
            // rest api has to be called stateless
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
