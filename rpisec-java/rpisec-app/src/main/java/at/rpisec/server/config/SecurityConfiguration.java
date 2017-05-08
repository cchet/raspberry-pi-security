package at.rpisec.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * This configuration class setups the security for this config.
 * <p>
 * See: http://www.baeldung.com/spring-security-custom-voter
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
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
            .antMatchers("/api/user/**").hasAnyRole(SecurityProperties.ADMIN)
            .antMatchers("/api/client/notify**").permitAll()
            .antMatchers("/api/client/**").hasAnyRole(SecurityProperties.CLIENT)
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return authManager;
    }
}
