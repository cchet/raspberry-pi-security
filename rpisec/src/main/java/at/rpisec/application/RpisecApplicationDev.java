package at.rpisec.application;

import at.rpisec.controller.LandingController;
import at.rpisec.jpa.model.User;
import at.rpisec.jpa.repositories.UserRepository;
import at.rpisec.rest.AliveRestService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackageClasses = {
        LandingController.class,
        AliveRestService.class
})
@EnableJpaRepositories(basePackageClasses = {
        User.class,
        UserRepository.class})
@EnableTransactionManagement
//@EnableScheduling // Needs to be configured, otherwise won't start
@Import({
        SecurityConfiguration.class,
        RestConfiguration.class
})
@Profile("dev")
public class RpisecApplicationDev {

    public static void main(String[] args) {
        SpringApplication.run(RpisecApplicationDev.class, args);
    }
}
