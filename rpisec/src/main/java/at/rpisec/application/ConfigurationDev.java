package at.rpisec.application;

import at.rpisec.jpa.model.User;
import at.rpisec.jpa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/16/17
 */
@Configuration
@Profile("dev")
public class ConfigurationDev {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepo;

    @Bean
    public CommandLineRunner produceCommandLineRunner() {
        return (args) -> {
            User admin = userRepo.findOne(1L);
            if (admin == null) {
                admin = new User();
                admin.setFirstname("Admin");
                admin.setLastname("Admin");
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin"));
                admin.setEmail("admin@rpisec.at");
                admin.setAdmin(Boolean.TRUE);

                userRepo.save(admin);
            }
        };
    }
}
