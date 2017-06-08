package at.rpisec.testsuite.client.test.rest.api;

import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.constants.SecurityConstants;
import at.rpisec.server.shared.rest.model.TokenResponse;
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

/**
 * This test class tests the client login via the rest api specified at @{link {@link ClientRestConstants#URI_CLIENT_LOGIN}}.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/04/17
 */
public class ClientLoginTests extends BaseIntegrationTest {

    @Test
    public void invalid_username() {
        // -- Given --
        final RestTemplate template = prepareRestTemplate("unknown", SecurityConstants.ADMIN);
        final URI url = UriComponentsBuilder.fromHttpUrl(AUTH_REST_API_BASE + ClientRestConstants.REL_CLIENT_LOGIN)
                                            .queryParam(ClientRestConstants.PARAM_DEVICE_ID, "myNewDevice").build().toUri();
        final HttpMethod method = HttpMethod.GET;

        // -- When --
        final ResponseEntity<TokenResponse> response = template.exchange(url, method, null, TokenResponse.class);

        // -- Then --
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertNotNull(response.getBody().getError());
    }

    @Test
    public void invalid_password() {
        // -- Given --
        final RestTemplate template = prepareRestTemplate(SecurityConstants.ADMIN, "unknown");
        final URI url = UriComponentsBuilder.fromHttpUrl(AUTH_REST_API_BASE + ClientRestConstants.REL_CLIENT_LOGIN)
                                            .queryParam(ClientRestConstants.PARAM_DEVICE_ID, "myNewDevice").build().toUri();
        final HttpMethod method = HttpMethod.GET;

        // -- When --
        final ResponseEntity<TokenResponse> response = template.exchange(url, method, null, TokenResponse.class);

        // -- Then --
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertNotNull(response.getBody().getError());
    }

    @Test
    public void invalid_no_device_id() {
        // -- Given --
        final RestTemplate template = prepareRestTemplate(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN);
        final URI url = UriComponentsBuilder.fromHttpUrl(AUTH_REST_API_BASE + ClientRestConstants.REL_CLIENT_LOGIN).build().toUri();
        final HttpMethod method = HttpMethod.GET;

        // -- When --
        final ResponseEntity<TokenResponse> response = template.exchange(url, method, null, TokenResponse.class);

        // -- Then --
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertNotNull(response.getBody().getError());
    }

    @Test
    public void invalid_empty_device_id() {
        // -- Given --
        final RestTemplate template = prepareRestTemplate(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN);
        final URI url = UriComponentsBuilder.fromHttpUrl(AUTH_REST_API_BASE + ClientRestConstants.REL_CLIENT_LOGIN)
                                            .queryParam(ClientRestConstants.PARAM_DEVICE_ID, "").build().toUri();
        final HttpMethod method = HttpMethod.GET;

        // -- When --
        final ResponseEntity<TokenResponse> response = template.exchange(url, method, null, TokenResponse.class);

        // -- Then --
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertNotNull(response.getBody().getError());
    }

    @Test
    public void valid_login() {
        // -- Given --
        final RestTemplate template = prepareRestTemplate(SecurityConstants.USER_ADMIN, SecurityConstants.USER_ADMIN);
        final URI url = UriComponentsBuilder.fromHttpUrl(AUTH_REST_API_BASE + ClientRestConstants.REL_CLIENT_LOGIN)
                                            .queryParam(ClientRestConstants.PARAM_DEVICE_ID, "myNewDevice").build().toUri();
        final HttpMethod method = HttpMethod.GET;

        // -- When --
        final ResponseEntity<TokenResponse> response = template.exchange(url, method, null, TokenResponse.class);

        // -- Then --
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertNotNull(response.getBody().getToken());
        Assert.assertNotNull(response.getBody().getCreated());
        Assert.assertNotNull(response.getBody().getClientId());
        Assert.assertNotNull(response.getBody().getClientSecret());
        Assert.assertNull(response.getBody().getError());
    }
}