package at.rpisec.application;

import at.rpisec.jpa.model.User;
import at.rpisec.jpa.repositories.UserRepository;
import at.rpisec.rest.AliveRestService;
import at.rpisec.rest.UserRestRepository;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.Filter;

@SpringBootApplication
@Configuration
@ComponentScan("at.rpisec")
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, UserRestRepository.class})
@EntityScan(basePackageClasses = User.class)
@EnableTransactionManagement
@Profile("dev")
public class RpisecApplicationDev {

    public static void main(String[] args) {
        SpringApplication.run(RpisecApplicationDev.class, args);
    }

    @Bean
    public Filter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(5120);

        return filter;
    }

    @Bean
    public ResourceConfig produceJerseyResourceConfig() {
        final ResourceConfig config = new ResourceConfig();
        config.packages(true, AliveRestService.class.getPackage().getName());

        return config;
    }

}
