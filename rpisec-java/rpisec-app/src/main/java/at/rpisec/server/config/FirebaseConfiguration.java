package at.rpisec.server.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.FirebaseDatabase;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/07/17
 */
@Configuration
public class FirebaseConfiguration {

    // See: https://firebase.google.com/docs/auth/admin/create-custom-tokens

    @Bean
    FirebaseApp produceFirebaseApp(final ConfigProperties.FirebaseProperties firebaseConfig) throws IOException {
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

    @Bean
    @Scope("prototype")
    FirebaseDatabase produceFirebaseDatabase(final FirebaseApp firebaseApp) {
        return FirebaseDatabase.getInstance(firebaseApp);
    }

    @Bean
    @Scope("prototype")
    @Qualifier("fcmRestTemplate")
    RestTemplate produceFcmRestTemplate(final ConfigProperties.FirebaseProperties firebaseConfig,
                                        final Logger log) {
        final RestTemplate fcmRestTemplate = new RestTemplate();
        fcmRestTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().put("Content-Type", Collections.singletonList("application/json"));
            request.getHeaders().put("Authorization", Collections.singletonList(String.format("key=%s", firebaseConfig.getCloudMsgApiKey())));
            return execution.execute(request, body);
        });
        fcmRestTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return !HttpStatus.OK.equals(response.getStatusCode());
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                log.error("Could not notify client applications. \nHttpStatus: {}\nBody: ", response.getStatusCode(), response.getBody());
            }
        });

        return fcmRestTemplate;
    }

    @Bean
    @Scope("prototype")
    @Qualifier("fcmSendUrl")
    String produceFcmSendUrl(final ConfigProperties.FirebaseProperties firebaseConfig) {
        return firebaseConfig.getCloudMessagingUrl();
    }
}
