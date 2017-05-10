package at.rpisec.oauth.config;

import at.rpisec.oauth.config.adapter.AuthorizationServerConfigurerAdapterImpl;
import at.rpisec.oauth.config.adapter.WebSecurityConfigurerAdapterImpl;
import at.rpisec.oauth.logic.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Configuration
public class SecurityConfiguration {

    @Bean
    WebSecurityConfigurerAdapter produceWebSecurityConfigurerAdapter() {
        return new WebSecurityConfigurerAdapterImpl();
    }

    @Bean
    AuthorizationServerConfigurerAdapter produceAuthorizationServerConfigurerAdapter() {
        return new AuthorizationServerConfigurerAdapterImpl();
    }

    @Bean
    PasswordEncoder createPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService produceUserDetailsManager() {
        return new UserDetailsServiceImpl();
    }
}
