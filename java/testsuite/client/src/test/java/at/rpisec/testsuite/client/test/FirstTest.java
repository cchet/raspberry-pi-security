package at.rpisec.testsuite.client.test;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.ShutdownStrategy;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/04/17
 */
@RunWith(JUnit4.class)
public class FirstTest {
    @ClassRule
    public static DockerComposeRule docker = DockerComposeRule.builder()
                                                              .saveLogsTo("build/docker-logs")
                                                              .file("build/docker/docker-compose.yml")
                                                              .pullOnStartup(true)
                                                              .shutdownStrategy(ShutdownStrategy.KILL_DOWN)
                                                              //.waitingForService("rpisec-test-app", HealthChecks.toRespondOverHttp(8080, (port) -> port.inFormat("https://$HOST:$EXTERNAL_PORT/rpisec-app/api/alive")))
                                                              //.waitingForService("rpisec-test-auth", HealthChecks.toRespondOverHttp(8080, (port) -> port.inFormat("https://$HOST:$EXTERNAL_PORT/rpisec-auth/api/alive")))
                                                              .build();

    @Test
    public void testThatUsesSomeDockerServices() {
        System.out.println("Test ran");
    }
}
