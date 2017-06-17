package at.rpisec.auth.security;

import at.rpisec.auth.jpa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The custom class for the token grant which requires an assignment between the user and the client device.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 05/12/17
 */
public class ClientUserPasswordTokenGranter extends AbstractTokenGranter {
    private static final String GRANT_TYPE = "password";

    @Autowired
    private UserRepository userRepo;

    private final AuthenticationManager authenticationManager;

    public ClientUserPasswordTokenGranter(AuthenticationManager authenticationManager,
                                          AuthorizationServerTokenServices tokenServices,
                                          ClientDetailsService clientDetailsService,
                                          OAuth2RequestFactory requestFactory) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }

    protected ClientUserPasswordTokenGranter(AuthenticationManager authenticationManager,
                                             AuthorizationServerTokenServices tokenServices,
                                             ClientDetailsService clientDetailsService,
                                             OAuth2RequestFactory requestFactory,
                                             String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client,
                                                           TokenRequest tokenRequest) {

        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String username = parameters.get("username");
        String password = parameters.get("password");
        // Protect from downstream leaks of password
        parameters.remove("password");

        // Check if user is assigned to given client
        if (userRepo.findByUsernameAndClientDeviceId(username, client.getClientId()) == null) {
            throw new InvalidGrantException("User is not assigned to given client");
        }

        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException e) {
            throw new InvalidGrantException(e.getMessage());
        }

        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user: " + username);
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
