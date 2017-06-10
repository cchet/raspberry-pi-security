package at.rpisec.testsuite.client.test.rest.api;

import at.rpisec.server.shared.rest.constants.SecurityConstants;
import at.rpisec.swagger.client.auth.api.ClientRestControllerApi;
import at.rpisec.swagger.client.auth.invoker.ApiException;
import at.rpisec.swagger.client.auth.invoker.ApiResponse;
import at.rpisec.testsuite.client.test.api.BaseIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/08/17
 */
public class RegisterFcmTokenTests extends BaseIntegrationTest {

    @Test
    public void invalid_username() throws Exception {
        // -- Given --
        final ClientRestControllerApi api = new ClientRestControllerApi(createAuthApiClient("unknown", SecurityConstants.USER_ADMIN));

        try {
            // -- When --
            api.registerFCMTokenUsingPUTWithHttpInfo(SecurityConstants.ADMIN, "myFcmToken");
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
            api.registerFCMTokenUsingPUTWithHttpInfo(SecurityConstants.ADMIN, "myFcmToken");
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
        api.registerFCMTokenUsingPUTWithHttpInfo(null, "myFcmToken");
    }

    // -- Then --
    @Test(expected = ApiException.class)
    public void invalid_client_no_fcm_token() throws Exception {
        // -- Given --
        final ClientRestControllerApi api = new ClientRestControllerApi(createAuthApiClient(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN));

        // -- When --
        api.registerFCMTokenUsingPUTWithHttpInfo(SecurityConstants.ADMIN, null);
    }


    // -- Then --
    @Test
    public void invalid_empty_device_id() throws Exception {
        // -- Given --
        final ClientRestControllerApi api = new ClientRestControllerApi(createAuthApiClient(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN));

        try {
            // -- When --
            api.registerFCMTokenUsingPUTWithHttpInfo("     ", "myFcmToken");
            Assert.fail("Expected ApiException here");
        } catch (ApiException e) {
            // -- Then --
            Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), e.getCode());
        }
    }

    // -- Then --
    @Test
    public void invalid_empty_fcm_token() throws Exception {
        // -- Given --
        final ClientRestControllerApi api = new ClientRestControllerApi(createAuthApiClient(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN));

        try {
            // -- When --
            api.registerFCMTokenUsingPUTWithHttpInfo(SecurityConstants.ADMIN, "    ");
            Assert.fail("Expected ApiException here");
        } catch (ApiException e) {
            // -- Then --
            Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), e.getCode());
        }
    }

    @Test
    public void invalid_unknown_device() {
        // -- Given --
        final ClientRestControllerApi api = new ClientRestControllerApi(createAuthApiClient(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN));

        try {
            // -- When --
            api.registerFCMTokenUsingPUTWithHttpInfo("unknownDevice", "myFcmToken");
            Assert.fail("Expected ApiException here");
        } catch (ApiException e) {
            // -- Then --
            Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), e.getCode());
        }
    }

    @Test
    public void valid() throws Exception {
        // -- Given --
        final ClientRestControllerApi api = new ClientRestControllerApi(createAuthApiClient(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN));

        // -- When --
        ApiResponse<Void> response = api.registerFCMTokenUsingPUTWithHttpInfo(SecurityConstants.ADMIN, "myFcmToken");

        // -- Then --
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }
}
