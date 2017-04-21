package at.rpisec.server.security;

import at.rpisec.server.logic.api.UserLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * This class performs a security check against the database.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/15/17
 */
public class DbUsernamePasswordAuthenticationManager implements AuthenticationManager {

    @Autowired
    private UserLogic userLogic;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = (authentication.getPrincipal() != null) ? authentication.getPrincipal().toString() : "";
        final String password = (authentication.getCredentials() != null) ? authentication.getCredentials().toString() : "";

        return userLogic.login(username, password);
    }
}
