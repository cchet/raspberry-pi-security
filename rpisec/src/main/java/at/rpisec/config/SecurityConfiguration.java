package at.rpisec.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * This configuration class setups the security for this config.
 * <p>
 * See: http://www.baeldung.com/spring-security-custom-voter
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/14/17
 */
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationManager authManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers("/rest/**").hasAnyRole(SecurityProperties.ADMIN, SecurityProperties.CLIENT)
            .and()
            .csrf().disable();
    }

    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return authManager;
    }
}
