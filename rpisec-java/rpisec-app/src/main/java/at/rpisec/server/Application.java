package at.rpisec.server;

import at.rpisec.server.config.ConfigurableMapperImpl;
import at.rpisec.server.jpa.model.Client;
import at.rpisec.server.jpa.repositories.ClientRepository;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Locale;

@SpringBootApplication
@ComponentScan(basePackageClasses = {Application.class, Jsr310JpaConverters.class})
@EnableWebMvc
@EnableJpaRepositories(basePackageClasses = {ClientRepository.class})
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableSwagger2
@EntityScan(basePackageClasses = Client.class)
@EnableScheduling
@EnableResourceServer
@Import({
        BeanValidatorPluginsConfiguration.class
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args)
                         .registerShutdownHook();
    }

    @Bean
    TaskScheduler produceTaskScheduler() {
        return new ThreadPoolTaskScheduler();
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
}
