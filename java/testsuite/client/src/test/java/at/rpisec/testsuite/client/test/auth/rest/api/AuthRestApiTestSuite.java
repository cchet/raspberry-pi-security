package at.rpisec.testsuite.client.test.auth.rest.api;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.ShutdownStrategy;
import com.palantir.docker.compose.connection.Container;
import com.palantir.docker.compose.connection.waiting.HealthCheck;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import com.palantir.docker.compose.connection.waiting.SuccessOrFailure;
import org.joda.time.Duration;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * The test class for the docker backed integration tests
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/08/17
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ClientLoginTests.class,
        RegisterFcmTokenTests.class
})
public class AuthRestApiTestSuite {

    private static final int TIMEOUT = Integer.valueOf(System.getProperty("timeout", "120000"));
    private static final boolean jenkinsTest = Boolean.valueOf(System.getProperty("jenkins", "false"));

    private static final Logger log = LoggerFactory.getLogger(AuthRestApiTestSuite.class);

    @ClassRule
    public static final DockerComposeRule dockerRule = DockerComposeRule.builder()
                                                                        .saveLogsTo("build/docker-logs")
                                                                        .file("build/docker/docker-compose.yml")
                                                                        .pullOnStartup(true)
                                                                        .removeConflictingContainersOnStartup(true)
                                                                        .shutdownStrategy(ShutdownStrategy.KILL_DOWN)
                                                                        .waitingForService("rpisec-test-nginx", createHealthCheckForApp())
                                                                        .waitingForService("rpisec-test-nginx", createHealthCheckForAuth())
                                                                        .nativeServiceHealthCheckTimeout(new Duration(TIMEOUT))
                                                                        .build();

    private static HealthCheck<Container> createHealthCheckForApp() {
        log.debug("Creating health check for app-service for testing environment: {}", jenkinsTest);
        return (jenkinsTest) ? createHealthCheckForAppJenkins() : createHealthCheckForAppLocal();
    }

    private static HealthCheck<Container> createHealthCheckForAuth() {
        log.debug("Creating health check for auth-service for testing environment: {}", jenkinsTest);
        return (jenkinsTest) ? createHealthCheckForAuthJenkins() : createHealthCheckForAuthLocal();
    }

    private static HealthCheck<Container> createHealthCheckForAuthJenkins() {
        return (container) -> {
            log.debug("Waiting for auth-service to become healthy");
            try {
                (new RestTemplate()).getForEntity("http://local_machine:9080/rpisec-auth/test/alive", null, Void.class);
                return SuccessOrFailure.success();
            } catch (RestClientException e) {
                return SuccessOrFailure.failure("Auth app not ready yet");
            }
        };
    }

    private static HealthCheck<Container> createHealthCheckForAppJenkins() {
        return (container) -> {
            log.debug("Waiting for app-service to become healthy");
            try {
                (new RestTemplate()).getForEntity("http://local_machine:9080/rpisec-app/test/alive", null, Void.class);
                return SuccessOrFailure.success();
            } catch (RestClientException e) {
                return SuccessOrFailure.failure("Auth app not ready yet");
            }
        };
    }

    private static HealthCheck<Container> createHealthCheckForAppLocal() {
        return HealthChecks.toRespond2xxOverHttp(8080, (port) -> port.inFormat("http://$HOST:$EXTERNAL_PORT/rpisec-app/test/alive"));
    }

    private static HealthCheck<Container> createHealthCheckForAuthLocal() {
        return HealthChecks.toRespond2xxOverHttp(8080, (port) -> port.inFormat("http://$HOST:$EXTERNAL_PORT/rpisec-auth/test/alive"));
    }
}
