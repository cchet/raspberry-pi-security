package at.rpisec.auth.config;

import at.rpisec.auth.config.other.ConfigProperties;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/07/17
 */
@Configuration
public class FirebaseConfiguration {

    @Autowired
    private ConfigProperties.FirebaseProperties firebaseConfig;

    @Bean
    FirebaseApp produceFirebaseApp() throws IOException {
        final File file = Paths.get(firebaseConfig.getConfigFile()).toFile();
        if (!file.exists()) {
            throw new IllegalArgumentException("firebaseConfig: '" + firebaseConfig.getConfigFile() + "' does not exist");
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredential(FirebaseCredentials.fromCertificate(FileUtils.openInputStream(file)))
                .setDatabaseUrl(firebaseConfig.getDatabaseUrl())
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    FirebaseAuth produceFirebaseAuth(final FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }
}
