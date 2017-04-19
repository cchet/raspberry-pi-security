package at.rpisec.config.dev;

import at.rpisec.config.ConfigProperties;
import at.rpisec.jpa.model.User;
import at.rpisec.jpa.repositories.UserRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
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

                userRepo.save(admin);
            }
        };
    }

    @Bean
    FirebaseApp produceFirebaseApp(final ConfigProperties.FirebaseProperties firebaseConfig) throws IOException {
        final File file = Paths.get(firebaseConfig.getConfigFile()).toFile();
        if (!file.exists()) {
            throw new IllegalArgumentException("firebaseConfig: '" + firebaseConfig.getConfigFile() + "' does not exist");
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(FileUtils.openInputStream(file))
                .setDatabaseUrl(firebaseConfig.getDatabaseUrl())
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    DatabaseReference produceDatabaseReference(FirebaseApp firebaseApp) {
        FirebaseDatabase.getInstance(firebaseApp).setPersistenceEnabled(false);
        return FirebaseDatabase.getInstance(firebaseApp).getReference();
    }
}
