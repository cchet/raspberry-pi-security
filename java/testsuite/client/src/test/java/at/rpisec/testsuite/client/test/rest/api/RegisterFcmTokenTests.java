package at.rpisec.testsuite.client.test.rest.api;

import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.constants.SecurityConstants;
import at.rpisec.testsuite.client.test.api.BaseIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/08/17
 */
public class RegisterFcmTokenTests extends BaseIntegrationTest {

    @Test
    public void invalid_username() {
        // -- Given --
        final RestTemplate template = prepareRestTemplate("unknown", SecurityConstants.USER_ADMIN);
        final URI url = UriComponentsBuilder.fromHttpUrl(AUTH_REST_API_BASE + ClientRestConstants.REL_URI_REGISTER_FCM_TOKEN)
                                            .queryParam(ClientRestConstants.PARAM_DEVICE_ID, SecurityConstants.USER_ADMIN)
                                            .queryParam(ClientRestConstants.PARAM_FCM_TOKEN, UUID.randomUUID().toString()).build().toUri();
        final HttpMethod method = HttpMethod.PUT;

        // -- When --
        final ResponseEntity<Void> response = template.exchange(url, method, null, Void.class);

        // -- Then --
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void invalid_password() {
        // -- Given --
        final RestTemplate template = prepareRestTemplate(SecurityConstants.USER_ADMIN, "unknown");
        final URI url = UriComponentsBuilder.fromHttpUrl(AUTH_REST_API_BASE + ClientRestConstants.REL_URI_REGISTER_FCM_TOKEN)
                                            .queryParam(ClientRestConstants.PARAM_DEVICE_ID, SecurityConstants.USER_ADMIN)
                                            .queryParam(ClientRestConstants.PARAM_FCM_TOKEN, UUID.randomUUID().toString()).build().toUri();
        final HttpMethod method = HttpMethod.PUT;

        // -- When --
        final ResponseEntity<Void> response = template.exchange(url, method, null, Void.class);

        // -- Then --
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void invalid_no_device_parameter() {
        // -- Given --
        final RestTemplate template = prepareRestTemplate(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN);
        final URI url = UriComponentsBuilder.fromHttpUrl(AUTH_REST_API_BASE + ClientRestConstants.REL_URI_REGISTER_FCM_TOKEN)
                                            .queryParam(ClientRestConstants.PARAM_FCM_TOKEN, UUID.randomUUID().toString()).build().toUri();
        final HttpMethod method = HttpMethod.PUT;

        // -- When --
        final ResponseEntity<Void> response = template.exchange(url, method, null, Void.class);

        // -- Then --
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void invalid_empty_device_parameter() {
        // -- Given --
        final RestTemplate template = prepareRestTemplate(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN);
        final URI url = UriComponentsBuilder.fromHttpUrl(AUTH_REST_API_BASE + ClientRestConstants.REL_URI_REGISTER_FCM_TOKEN)
                                            .queryParam(ClientRestConstants.PARAM_DEVICE_ID, "")
                                            .queryParam(ClientRestConstants.PARAM_FCM_TOKEN, UUID.randomUUID().toString()).build().toUri();
        final HttpMethod method = HttpMethod.PUT;

        // -- When --
        final ResponseEntity<Void> response = template.exchange(url, method, null, Void.class);

        // -- Then --
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void invalid_no_token_parameter() {
        // -- Given --
        final RestTemplate template = prepareRestTemplate(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN);
        final URI url = UriComponentsBuilder.fromHttpUrl(AUTH_REST_API_BASE + ClientRestConstants.REL_URI_REGISTER_FCM_TOKEN)
                                            .queryParam(ClientRestConstants.PARAM_DEVICE_ID, SecurityConstants.USER_ADMIN).build().toUri();
        final HttpMethod method = HttpMethod.PUT;

        // -- When --
        final ResponseEntity<Void> response = template.exchange(url, method, null, Void.class);

        // -- Then --
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void invalid_empty_token_parameter() {
        // -- Given --
        final RestTemplate template = prepareRestTemplate(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN);
        final URI url = UriComponentsBuilder.fromHttpUrl(AUTH_REST_API_BASE + ClientRestConstants.REL_URI_REGISTER_FCM_TOKEN)
                                            .queryParam(ClientRestConstants.PARAM_DEVICE_ID, SecurityConstants.USER_ADMIN)
                                            .queryParam(ClientRestConstants.PARAM_FCM_TOKEN, "").build().toUri();
        final HttpMethod method = HttpMethod.PUT;

        // -- When --
        final ResponseEntity<Void> response = template.exchange(url, method, null, Void.class);

        // -- Then --
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void invalid_unknown_device() {
        // -- Given --
        final RestTemplate template = prepareRestTemplate(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN);
        final URI url = UriComponentsBuilder.fromHttpUrl(AUTH_REST_API_BASE + ClientRestConstants.REL_URI_REGISTER_FCM_TOKEN)
                                            .queryParam(ClientRestConstants.PARAM_DEVICE_ID, "unknownDeviceId")
                                            .queryParam(ClientRestConstants.PARAM_FCM_TOKEN, UUID.randomUUID().toString()).build().toUri();
        final HttpMethod method = HttpMethod.PUT;

        // -- When --
        final ResponseEntity<Void> response = template.exchange(url, method, null, Void.class);

        // -- Then --
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void valid() {
        // -- Given --
        final RestTemplate template = prepareRestTemplate(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN);
        final URI url = UriComponentsBuilder.fromHttpUrl(AUTH_REST_API_BASE + ClientRestConstants.REL_URI_REGISTER_FCM_TOKEN)
                                            .queryParam(ClientRestConstants.PARAM_DEVICE_ID, SecurityConstants.USER_ADMIN)
                                            .queryParam(ClientRestConstants.PARAM_FCM_TOKEN, UUID.randomUUID().toString()).build().toUri();
        final HttpMethod method = HttpMethod.PUT;

        // -- When --
        final ResponseEntity<Void> response = template.exchange(url, method, null, Void.class);

        // -- Then --
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
