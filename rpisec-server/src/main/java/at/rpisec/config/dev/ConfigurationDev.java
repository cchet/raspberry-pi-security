package at.rpisec.config.dev;

import at.rpisec.config.ConfigProperties;
import at.rpisec.jpa.model.User;
import at.rpisec.jpa.repositories.UserRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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
    CommandLineRunner produceCommandLineRunner() {
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

                final User client = new User();
                client.setFirstname("Client_1");
                client.setLastname("Client_1");
                client.setUsername("client");
                client.setPassword(encoder.encode("client"));
                client.setEmail("client_1@rpisec.at");
                client.setAdmin(Boolean.FALSE);

                userRepo.save(admin);
                userRepo.save(client);
            }
        };
    }
}
