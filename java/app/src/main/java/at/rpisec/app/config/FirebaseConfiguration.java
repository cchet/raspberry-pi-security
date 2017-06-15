package at.rpisec.app.config;

import at.rpisec.app.config.other.ConfigProperties;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.FirebaseDatabase;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * This class holds the producers for the firebase related beans.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/07/17
 */
@Configuration
public class FirebaseConfiguration {

    @Autowired
    private ConfigProperties.FirebaseProperties firebaseConfig;

    public static final String REST_TEMPLATE_FCM = "REST_TEMPLATE_FCM";
    public static final String FCM_URL = "FCM_URL";

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
    @Scope("prototype")
    FirebaseDatabase produceFirebaseDatabase(final FirebaseApp firebaseApp) {
        return FirebaseDatabase.getInstance(firebaseApp);
    }

    @Bean
    @Scope("prototype")
    @Qualifier(REST_TEMPLATE_FCM)
    RestTemplate produceFcmRestTemplate(final Logger log) {
        final RestTemplate fcmRestTemplate = new RestTemplate();
        fcmRestTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
            request.getHeaders().put(HttpHeaders.AUTHORIZATION, Collections.singletonList(String.format("key=%s", firebaseConfig.getCloudMsgApiKey())));
            return execution.execute(request, body);
        });
        fcmRestTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return !HttpStatus.OK.equals(response.getStatusCode());
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                log.error("Could not notify client via FCM. HttpStatus: {} / Body: {}", response.getStatusCode(), response.getBody());
            }
        });

        return fcmRestTemplate;
    }

    @Bean
    @Scope("prototype")
    @Qualifier(FCM_URL)
    String produceFcmSendUrl() {
        return firebaseConfig.getCloudMessagingUrl();
    }
}
