package at.rpisec.auth;

import at.rpisec.auth.config.other.ConfigProperties;
import at.rpisec.auth.config.other.ConfigurableMapperImpl;
import at.rpisec.auth.jpa.model.User;
import at.rpisec.auth.jpa.repositories.UserRepository;
import at.rpisec.swagger.client.app.client.api.InternalRestControllerApi;
import at.rpisec.swagger.client.app.client.invoker.ApiClient;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
@EntityScan(basePackageClasses = User.class)
@EnableJpaRepositories(basePackageClasses = {UserRepository.class})
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableSwagger2
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args)
                         .registerShutdownHook();
    }

    @Bean
    @Scope("prototype")
    Logger logger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
    }

    @Bean
    MapperFacade produceConfigurableMapper() {
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

    @Bean
    @Qualifier("appServer")
    ApiClient produceAppServerApiClient(final ConfigProperties.RpisecProperties rpisecProperties){
        final ApiClient client = new ApiClient();
        client.setUsername(rpisecProperties.getSystemUser());
        client.setPassword(rpisecProperties.getSystemPassword());
        client.setBasePath(rpisecProperties.getBaseUrl());

        return client;
    }

    @Bean
    InternalRestControllerApi produceInternalRestControllerApi(final @Qualifier("appServer") ApiClient client){
        return new InternalRestControllerApi(client);
    }
}
