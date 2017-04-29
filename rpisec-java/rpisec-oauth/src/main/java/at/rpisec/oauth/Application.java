package at.rpisec.oauth;

import at.rpisec.oauth.config.AuthorizationServerConfig;
import at.rpisec.oauth.config.WebSecurityConfigDev;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/28/17
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = Application.class)
@EnableAuthorizationServer
@EnableWebMvc
@Import({
        WebSecurityConfigDev.class,
        AuthorizationServerConfig.class,
        WebSecurityConfigDev.class
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
