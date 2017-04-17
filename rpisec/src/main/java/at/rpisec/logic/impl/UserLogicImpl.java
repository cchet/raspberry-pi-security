package at.rpisec.logic.impl;

import at.rpisec.config.SecurityProperties;
import at.rpisec.jpa.model.User;
import at.rpisec.jpa.repositories.UserRepository;
import at.rpisec.logic.api.UserLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/17/17
 */
@Service
@Transactional
public class UserLogicImpl implements UserLogic {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepo;

    @Override
    @Transactional(readOnly = true)
    public Authentication login(final String username,
                                final String rawPassword) {
        Objects.requireNonNull(username, "Cannot login user with null username");
        Objects.requireNonNull(rawPassword, "Cannot login user with null password");

        final User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("User with username '" + username + "' does not exist");
        }
        if (!encoder.matches(rawPassword, user.getPassword())) {
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
