package at.rpisec;

import at.rpisec.config.RestConfiguration;
import at.rpisec.config.SecurityConfiguration;
import at.rpisec.jpa.model.User;
import at.rpisec.jpa.repositories.UserRepository;
import at.rpisec.rest.UserRestRepository;
import at.rpisec.security.DbUsernamePasswordAuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan("at.rpisec")
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, UserRestRepository.class})
@EntityScan(basePackageClasses = User.class)
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableSwagger2
@Import({
        SpringDataRestConfiguration.class,
        BeanValidatorPluginsConfiguration.class
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    RepositoryRestConfigurerAdapter produceRestResourceConfigurerAdaptor() {
        return new RestConfiguration();
    }

    @Bean
    WebSecurityConfigurerAdapter produceWebSecurityConfigurerAdapter() {
        return new SecurityConfiguration();
    }

    @Bean
    Docket produceSwaggerDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/rpisec")
                .select()
                .paths(PathSelectors.ant("/rest/**"))
                .build();
    }

    @Bean
    AuthenticationManager produceAuthManager() {
        return new DbUsernamePasswordAuthenticationManager();
    }

    @Bean
    PasswordEncoder producePasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Scope("prototype")
    Logger logger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
    }
}
