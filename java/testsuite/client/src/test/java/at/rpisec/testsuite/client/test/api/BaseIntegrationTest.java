package at.rpisec.testsuite.client.test.api;

import at.rpisec.swagger.client.auth.invoker.ApiClient;
import org.junit.Before;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Collections;
import java.util.Objects;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/05/17
 */
public class BaseIntegrationTest {

    protected static final String AUTH_BASE = "http://localhost:9080/rpisec-auth";
    protected static final String AUTH_REST_SYSTEM_API_BASE = AUTH_BASE + "/test";

    @Before
    public void before() {
        final RestTemplate template = prepareRestTemplate(null, null);
        final ResponseEntity<Void> response = template.exchange(AUTH_REST_SYSTEM_API_BASE + "/prepare", HttpMethod.POST, null, Void.class);
        if (!HttpStatus.OK.equals(response.getStatusCode())) {
            throw new IllegalStateException("Could not clear auth server state after test");
        }
    }

    protected ApiClient createAuthApiClient(final String username,
                                            final String password) {
        return createApiClient(AUTH_BASE, username, password);
    }

    protected ApiClient createApiClient(final String baseUrl,
                                        final String username,
                                        final String password) {
        Objects.requireNonNull(baseUrl, "BaseUrl must not be null");
        Objects.requireNonNull(username, "Username must not be null");
        Objects.requireNonNull(password, "Password must not be null");

        final ApiClient client = new ApiClient();
        client.setUsername(username);
        client.setPassword(password);
        client.setBasePath(baseUrl);

        return client;
    }

    /**
     * Prepares a rest template with the given username and password set as basic auth.
     *
     * @param username the user's username
     * @param password the user's password
     * @return the prepared rest template
     */
    private RestTemplate prepareRestTemplate(final String username,
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
