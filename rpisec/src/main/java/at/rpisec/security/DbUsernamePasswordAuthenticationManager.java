package at.rpisec.security;

import at.rpisec.application.SecurityProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

/**
 * This class performs a security check against the database.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/15/17
 */
public class DbUsernamePasswordAuthenticationManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = (authentication.getPrincipal() != null) ? authentication.getPrincipal().toString() : "";
        final String password = (authentication.getCredentials() != null) ? authentication.getCredentials().toString() : "";

        // TODO: Check user against database and create proper GrantedAuthorities

        return new UsernamePasswordAuthenticationToken(username,
                                                       password,
                                                       Arrays.asList(new SimpleGrantedAuthority(SecurityProperties.ROLE_ADMIN), new SimpleGrantedAuthority(SecurityProperties.ROLE_CLIENT)));
    }
}
