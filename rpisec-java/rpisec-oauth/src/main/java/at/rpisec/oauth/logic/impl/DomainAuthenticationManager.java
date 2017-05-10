package at.rpisec.oauth.logic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class performs a security check against the database.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/15/17
 */
public class DomainAuthenticationManager implements AuthenticationManager {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = (authentication.getPrincipal() != null) ? authentication.getPrincipal().toString() : "";
        final String password = (authentication.getCredentials() != null) ? authentication.getCredentials().toString() : "";

        final UserDetails user = userDetailsService.loadUserByUsername(username);

        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("User account is not valid anymore");
        }
        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is disabled");
        }
        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("Credentials expired");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("User account is disabled");
        }
        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return new UsernamePasswordAuthenticationToken(username, user.getPassword(), user.getAuthorities());
    }
}
