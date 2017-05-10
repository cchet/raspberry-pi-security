package at.rpisec.oauth;

import at.rpisec.oauth.config.ConfigurableMapperImpl;
import at.rpisec.oauth.config.StartupRunner;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

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
    CommandLineRunner produceStartupRunner() {
        return new StartupRunner();
    }

    @Bean
    public MapperFacade produceConfigurableMapper() {
        return new ConfigurableMapperImpl();
    }

    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages/message", "classpath:messages/email", "classpath:messages/validation");
        messageSource.setDefaultEncoding("UTF8");
        messageSource.setCacheSeconds(60 * 60 * 24);
        return messageSource;
    }

    @Bean("localeResolver")
    LocaleResolver produceLocaleResolver() {
        return new FixedLocaleResolver(Locale.US);
    }

    /**
     * Builds the baseUrl of the hosted application
     *
     * @param baseUrl     the base url to use in the format e.g. 'http://server:8080'
     * @param contextPath the server.context-path property value
     * @return the build server root url
     */
    @Bean("baseUrl")
    String produceBaseUrl(@Value("${link.baseUrl}") String baseUrl,
                          @Value("${server.context-path}") String contextPath) {
        return baseUrl + contextPath;
    }

}
