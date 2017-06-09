package at.rpisec.oauth.config.adapter;

import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.constants.SecurityConstants;
import at.rpisec.server.shared.rest.constants.UserRestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
public class WebSecurityConfigurerAdapterImpl extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Exclude test and oauth uris
        http.authorizeRequests().antMatchers("/oauth/**").permitAll()
            .and().authorizeRequests().antMatchers("/test/**").permitAll()
            // enable basic auth for all resources under /api/client and /api/user
            .and().httpBasic()
            .and().authorizeRequests().antMatchers(ClientRestConstants.BASE_URI + "/**").hasAnyRole(SecurityConstants.ADMIN, SecurityConstants.CLIENT)
            .and().authorizeRequests().antMatchers(UserRestConstants.REST_BASE + "/**").hasAnyRole(SecurityConstants.ADMIN)
            // We have rest so need for cross site request forgery protection
            .and().csrf().disable()
            // rest api has to be called stateless
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return authenticationManager;
    }
}
