package at.rpisec.server.config;

import at.rpisec.server.security.DbUsernamePasswordAuthenticationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Configuration
public class SecurityConfiguration {

    @Bean
    AuthenticationManager produceAuthManager() {
        return new DbUsernamePasswordAuthenticationManager();
    }

    @Bean
    PasswordEncoder producePasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    WebSecurityConfigurerAdapter produceWebSecurityConfigurerAdapter() {
        return new WebSecurityConfigurerAdapterImpl();
    }
}
