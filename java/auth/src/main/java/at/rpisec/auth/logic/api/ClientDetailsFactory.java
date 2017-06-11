package at.rpisec.auth.logic.api;

import at.rpisec.shared.rest.constants.SecurityConstants;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Factory class for producing oauth client details instances for different client types.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 05/12/17
 */
public class ClientDetailsFactory {

    private ClientDetailsFactory() {
    }

    /**
     * Creates a mobile oauth client.
     *
     * @param clientId
     * @param clientSecret
     * @param issuer
     * @param username
     * @param device
     * @param resourceIds
     * @return
     */
    public static ClientDetails createMobileClientDetails(final String clientId,
                                                          final String clientSecret,
                                                          final String issuer,
                                                          final String username,
                                                          final String device,
                                                          final List<String> resourceIds) {
        final BaseClientDetails client = new BaseClientDetails(clientId,
                                                               "",
                                                               "",
                                                               "",
                                                               SecurityConstants.CLIENT);
        client.setResourceIds(resourceIds);
        client.setAccessTokenValiditySeconds(SecurityConstants.TOKEN_VALIDITY_DURATION_SECONDS);
        client.setRefreshTokenValiditySeconds(SecurityConstants.REFRESH_TOKEN_VALIDITY_DURATION_SECONDS);
        client.setClientSecret(clientSecret);
        client.setScope(Arrays.asList(SecurityConstants.SCOPE_READ, SecurityConstants.SCOPE_WRITE));
        client.setAuthorizedGrantTypes(Arrays.asList("password", "authorization_code", "implicit"));

        final Map<String, String> additionalInformation = new HashMap<>();
        additionalInformation.put("Issuer", issuer);
        additionalInformation.put("Username", username);
        additionalInformation.put("Device", device);
        additionalInformation.put("created", LocalDateTime.now().toString());
        additionalInformation.put("Modified", LocalDateTime.now().toString());
        client.setAdditionalInformation(additionalInformation);

        return client;
    }

    /**
     * Creates a rpisec app server oauth client.
     *
     * @param clientId
     * @param clientSecret
     * @param issuer
     * @param username
     * @param resourceIds
     * @return
     */
    public static ClientDetails createAppClientDetails(final String clientId,
                                                       final String clientSecret,
                                                       final String issuer,
                                                       final String username,
                                                       final List<String> resourceIds) {
        final BaseClientDetails client = new BaseClientDetails(clientId,
                                                               "",
                                                               "",
                                                               "",
                                                               SecurityConstants.SYSTEM);
        client.setResourceIds(resourceIds);
        client.setAccessTokenValiditySeconds(SecurityConstants.TOKEN_VALIDITY_DURATION_SECONDS);
        client.setRefreshTokenValiditySeconds(SecurityConstants.REFRESH_TOKEN_VALIDITY_DURATION_SECONDS);
        client.setClientSecret(clientSecret);
        client.setScope(Collections.singletonList(SecurityConstants.SCOPE_TRUST));
        client.setAuthorizedGrantTypes(Collections.singletonList("client_credentials"));
        client.setAutoApproveScopes(client.getScope());

        final Map<String, String> additionalInformation = new HashMap<>();
        additionalInformation.put("Issuer", issuer);
        additionalInformation.put("Username", username);
        additionalInformation.put("Device", "Rpisec-App-Instance");
        additionalInformation.put("created", LocalDateTime.now().toString());
        additionalInformation.put("Modified", LocalDateTime.now().toString());
        client.setAdditionalInformation(additionalInformation);

        return client;
    }
}
