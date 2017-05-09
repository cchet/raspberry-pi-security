package at.rpisec.oauth;

import at.rpisec.oauth.config.AuthorizationServerConfig;
import at.rpisec.oauth.config.WebSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Doc-url: https://github.com/spring-projects/spring-security-oauth/blob/master/docs/oauth2.md
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/28/17
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = Application.class)
@EnableAuthorizationServer
@EnableWebMvc
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    AuthorizationServerConfigurerAdapter produceAuthorizationServerConfigurerAdapter() {
        return new AuthorizationServerConfig();
    }

    @Bean
    WebSecurityConfigurerAdapter produceWebSecurityConfigurerAdapter() {
        return new WebSecurityConfig();
    }

    @Bean
    PasswordEncoder createPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
