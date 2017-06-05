package at.rpisec.testsuite.client.test;

import at.rpisec.testsuite.client.test.api.BaseIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/04/17
 */
@RunWith(JUnit4.class)
public class FirstTest extends BaseIntegrationTest {

    @Test
    public void testThatUsesSomeDockerServices() {
        System.out.println("Test ran");
    }
}
