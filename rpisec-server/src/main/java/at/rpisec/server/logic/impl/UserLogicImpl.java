package at.rpisec.server.logic.impl;

import at.rpisec.server.config.SecurityProperties;
import at.rpisec.server.jpa.model.User;
import at.rpisec.server.jpa.repositories.UserRepository;
import at.rpisec.server.logic.event.UserEventHandler;
import at.rpisec.server.logic.api.UserLogic;
import at.rpisec.server.rest.model.UserDto;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import java.util.UUID;

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
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    public MapperFacade mapper;

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

    @Override
    public UserDto byId(Long id) {
        final UserDto model = new UserDto();
        model.setFirstname("thomas");

        return model;
    }

    @Override
    public List<UserDto> list() {
        return null;
    }

    @Override
    public UserDto create(UserDto model) {
        User user = mapper.map(model, User.class);
        user.setAdmin(false);
        user.setVerifyUUID(UUID.randomUUID().toString());

        user = userRepo.save(user);

        publisher.publishEvent(new UserEventHandler.UserCreatedEvent(user.getEmail(),
                                                                     (user.getLastname() + ", " + user.getFirstname()),
                                                                     user.getVerifyUUID()));

        return mapper.map(user, UserDto.class);
    }

    @Override
    public UserDto update(UserDto model) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
