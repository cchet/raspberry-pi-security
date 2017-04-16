package at.rpisec.security;

import at.rpisec.application.SecurityProperties;
import at.rpisec.jpa.model.User;
import at.rpisec.jpa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * This class performs a security check against the database.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/15/17
 */
public class DbUsernamePasswordAuthenticationManager implements AuthenticationManager {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = (authentication.getPrincipal() != null) ? authentication.getPrincipal().toString() : "";
        final String password = (authentication.getCredentials() != null) ? authentication.getCredentials().toString() : "";

        final User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("User with username '" + username + "' does not exist");
        }
        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Password invalid");
        }

        final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(SecurityProperties.ROLE_CLIENT));
        if (user.getAdmin()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(SecurityProperties.ROLE_ADMIN));
        }

        return new UsernamePasswordAuthenticationToken(username, user.getPassword(), grantedAuthorities);
    }
}
