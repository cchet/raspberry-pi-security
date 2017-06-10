package at.rpisec.testsuite.client.test.rest.api;

import at.rpisec.server.shared.rest.constants.SecurityConstants;
import at.rpisec.swagger.client.auth.api.ClientRestControllerApi;
import at.rpisec.swagger.client.auth.invoker.ApiException;
import at.rpisec.swagger.client.auth.invoker.ApiResponse;
import at.rpisec.swagger.client.auth.model.TokenResponse;
import at.rpisec.testsuite.client.test.api.BaseIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

/**
 * This test class tests the client login via the rest api specified at @{link {@link at.rpisec.server.shared.rest.constants.AuthRestConstants#REL_CLIENT_LOGIN}}.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/04/17
 */
public class ClientLoginTests extends BaseIntegrationTest {

    @Test
    public void invalid_username() throws Exception {
        // -- Given --
        final ClientRestControllerApi api = new ClientRestControllerApi(createAuthApiClient("unknown", SecurityConstants.USER_ADMIN));

        try {
            // -- When --
            api.loginUsingGETWithHttpInfo("myDeviceNew");
            Assert.fail("Expected ApiException here");
        } catch (ApiException e) {
            // -- Then --
            Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), e.getCode());
        }
    }

    @Test
    public void invalid_password() throws Exception {
        // -- Given --
        final ClientRestControllerApi api = new ClientRestControllerApi(createAuthApiClient(SecurityConstants.USER_ADMIN, "unknown"));

        try {
            // -- When --
            api.loginUsingGETWithHttpInfo("myDeviceNew");
            Assert.fail("Expected ApiException here");
        } catch (ApiException e) {
            // -- Then --
            Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), e.getCode());
        }
    }

    // -- Then --
    @Test(expected = ApiException.class)
    public void invalid_client_no_device_id() throws Exception {
        // -- Given --
        final ClientRestControllerApi api = new ClientRestControllerApi(createAuthApiClient(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN));

        // -- When --
        api.loginUsingGETWithHttpInfo(null);
    }

    // -- Then --
    @Test
    public void invalid_empty_device_id() throws Exception {
        // -- Given --
        final ClientRestControllerApi api = new ClientRestControllerApi(createAuthApiClient(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN));

        try {
            // -- When --
            api.loginUsingGETWithHttpInfo("      ");
            Assert.fail("Expected ApiException here");
        } catch (ApiException e) {
            // -- Then --
            Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), e.getCode());
        }
    }

    @Test
    public void valid_login() throws Exception {
        // -- Given --
        final ClientRestControllerApi api = new ClientRestControllerApi(createAuthApiClient(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN));

        // -- When --
        final ApiResponse<TokenResponse> response = api.loginUsingGETWithHttpInfo("MyDevice");

        // -- Then --
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assert.assertNotNull(response.getData());
        Assert.assertNotNull(response.getData().getToken());
        Assert.assertNotNull(response.getData().getCreated());
        Assert.assertNotNull(response.getData().getClientId());
        Assert.assertNotNull(response.getData().getClientSecret());
        Assert.assertNull(response.getData().getError());
    }
}