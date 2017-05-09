package at.rpisec.oauth;

import at.rpisec.oauth.config.StartupRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
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
    @Scope("prototype")
    Logger logger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
    }

    @Bean
    CommandLineRunner produceStartupRunner(){
        return new StartupRunner();
    }
}
