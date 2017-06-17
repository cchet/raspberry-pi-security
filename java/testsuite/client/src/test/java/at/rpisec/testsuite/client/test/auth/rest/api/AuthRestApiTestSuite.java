package at.rpisec.testsuite.client.test.auth.rest.api;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.ShutdownStrategy;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import org.joda.time.Duration;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/08/17
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ClientLoginTests.class,
        RegisterFcmTokenTests.class
})
public class AuthRestApiTestSuite {

    @ClassRule
    public static final DockerComposeRule dockerRule = DockerComposeRule.builder()
                                                                        .saveLogsTo("build/docker-logs")
                                                                        .file("build/docker/docker-compose.yml")
                                                                        .pullOnStartup(true)
                                                                        .removeConflictingContainersOnStartup(true)
                                                                        .shutdownStrategy(ShutdownStrategy.KILL_DOWN)
                                                                        .waitingForService("rpisec-test-nginx",
                                                                                           HealthChecks.toRespond2xxOverHttp(8080,
                                                                                                                             (port) -> port.inFormat(
                                                                                                                                     "http://$HOST:$EXTERNAL_PORT/rpisec-app/test/alive")))
                                                                        .waitingForService("rpisec-test-nginx",
                                                                                           HealthChecks.toRespond2xxOverHttp(8080,
                                                                                                                             (port) -> port.inFormat(
                                                                                                                                     "http://$HOST:$EXTERNAL_PORT/rpisec-auth/test/alive")))
                                                                        .nativeServiceHealthCheckTimeout(new Duration(600000))
                                                                        .build();

}
