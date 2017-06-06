package at.rpisec.testsuite.client.test.api;

import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.ShutdownStrategy;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import org.joda.time.Duration;
import org.junit.ClassRule;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Collections;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/05/17
 */
public class BaseIntegrationTest {

    protected static final String AUTH_REST_API_BASE = "http://localhost:9080/rpisec-auth" + ClientRestConstants.BASE_URI;

    @ClassRule
    public static final DockerComposeRule docker = DockerComposeRule.builder()
                                                                    .saveLogsTo("build/docker-logs")
                                                                    .file("build/docker/docker-compose.yml")
                                                                    .pullOnStartup(true)
                                                                    .removeConflictingContainersOnStartup(true)
                                                                    .shutdownStrategy(ShutdownStrategy.KILL_DOWN)
                                                                    .waitingForService("rpisec-test-nginx",
                                                                                       HealthChecks.toRespond2xxOverHttp(8080,
                                                                                                                         (port) -> port.inFormat(
                                                                                                                                 "http://$HOST:$EXTERNAL_PORT/rpisec-app/api/system/alive")))
                                                                    .waitingForService("rpisec-test-nginx",
                                                                                       HealthChecks.toRespond2xxOverHttp(8080,
                                                                                                                         (port) -> port.inFormat(
                                                                                                                                 "http://$HOST:$EXTERNAL_PORT/rpisec-auth/api/system/alive")))
                                                                    .nativeServiceHealthCheckTimeout(new Duration(20000))
                                                                    .build();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (docker != null) {
                try {
                    docker.dockerCompose().kill();
                } catch (Exception e) {
                }
            }
        }));
    }

    /**
     * Prepares a plain rest template.
     *
     * @return the prepared rest template
     */
    protected RestTemplate prepareRestTemplate() {
        return prepareRestTemplate(null, null);
    }

    /**
     * Prepares a rest template with the given username and password set as basic auth.
     *
     * @param username the user's username
     * @param password the user's password
     * @return the prepared rest template
     */
    protected RestTemplate prepareRestTemplate(final String username,
                                               final String password) {
        final RestTemplate template = new RestTemplate();
        if ((username != null) && (password != null)) {
            final String auth = username + ":" + password;
            final String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(Charset.forName("US-ASCII")));
            template.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().put(HttpHeaders.AUTHORIZATION, Collections.singletonList(String.format("Basic %s", encodedAuth)));
                return execution.execute(request, body);
            });
        }

        template.getMessageConverters().add(new FormHttpMessageConverter());
        // We want to handle response codes ourselfs in the test methods and not have a exception to be thrown as the default used handler does
        template.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
            }
        });

        return template;
    }
}
