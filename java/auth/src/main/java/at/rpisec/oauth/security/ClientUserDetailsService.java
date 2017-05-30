package at.rpisec.oauth.security;

import at.rpisec.oauth.jpa.model.User;
import at.rpisec.oauth.jpa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/10/17
 */
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
public class ClientUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username != null) {
            final User user = userRepo.findByUsernameAndVerifiedAtNotNull(username);
            if (user != null) {
                return new UserDetails() {

                    private final LocalDateTime now = LocalDateTime.now();

                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                    }

                    @Override
                    public String getPassword() {
                        return user.getPassword();
                    }

                    @Override
                    public String getUsername() {
                        return user.getUsername();
                    }

                    @Override
                    public boolean isAccountNonExpired() {
                        return !user.getDeactivated();
                    }

                    @Override
                    public boolean isAccountNonLocked() {
                        return user.getVerifiedAt() != null;
                    }

                    @Override
                    public boolean isCredentialsNonExpired() {
                        return now.isBefore(user.getPasswordValidityDate());
                    }

                    @Override
                    public boolean isEnabled() {
                        return !user.getDeactivated();
                    }
                };
            }
        }

        throw new UsernameNotFoundException(String.format("User with username '%s' not found", username));
    }
}
