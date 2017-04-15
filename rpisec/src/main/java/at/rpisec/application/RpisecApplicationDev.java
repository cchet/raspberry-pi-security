package at.rpisec.application;

import at.rpisec.jpa.model.User;
import at.rpisec.jpa.repositories.UserRepository;
import at.rpisec.rest.UserRestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.Filter;

@SpringBootApplication
@Configuration
@ComponentScan("at.rpisec")
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, UserRestRepository.class})
@EntityScan(basePackageClasses = User.class)
@EnableTransactionManagement
@EnableAspectJAutoProxy
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
    public CommandLineRunner produceCommandLineRunner(final UserRepository userRepo) {
        return (args) -> {
            User admin = userRepo.findOne(1L);
            if (admin == null) {
                admin = new User();
                admin.setFirstname("Admin");
                admin.setLastname("Admin");
                admin.setUsername("admin");
                admin.setPassword("admin");
                admin.setEmail("admin@rpisec.at");
                admin.setAdmin(Boolean.TRUE);

                userRepo.save(admin);
            }
        };
    }
}
