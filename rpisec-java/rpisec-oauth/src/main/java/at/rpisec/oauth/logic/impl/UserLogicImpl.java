package at.rpisec.oauth.logic.impl;

import at.rpisec.oauth.config.SecurityConfiguration;
import at.rpisec.oauth.exception.DbEntryNotFoundException;
import at.rpisec.oauth.jpa.model.User;
import at.rpisec.oauth.jpa.repositories.UserRepository;
import at.rpisec.oauth.logic.api.UserLogic;
import at.rpisec.oauth.logic.event.UserVerifiedEvent;
import at.rpisec.oauth.logic.event.UserCreatedEvent;
import at.rpisec.server.shared.rest.constants.SecurityConstants;
import at.rpisec.server.shared.rest.model.UserDto;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/17/17
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@Secured(SecurityConstants.ROLE_ADMIN)
public class UserLogicImpl implements UserLogic {

    @Autowired
    @Qualifier(SecurityConfiguration.QUALIFIER_APP_SERVER_RESOURCE_ID)
    private String resourceId;
    @Autowired
    private PasswordEncoder pwdEncoder;
    @Autowired
    private ClientRegistrationService clientRegistrationService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    public MapperFacade mapper;

    @Override
    public Long verifyAccount(String uuid,
                              String password) {
        Objects.requireNonNull(uuid, "Cannot load user with null uuid");

        final User user = userRepo.findByVerifyUUID(uuid);
        if (user == null) {
            throw new DbEntryNotFoundException(String.format("User not found by uuid: '%s' during verify", uuid), User.class);
        }

        user.setVerifyUUID(null);
        user.setVerifiedAt(LocalDateTime.now());
        user.setPassword(pwdEncoder.encode(password));

        userRepo.saveAndFlush(user);

        final String secret = UUID.randomUUID().toString();
        final BaseClientDetails client = new BaseClientDetails(UUID.randomUUID().toString(),
                                                               resourceId,
                                                               "read,write",
                                                               "password,authorization_code",
                                                               SecurityConstants.CLIENT);
        client.setAccessTokenValiditySeconds(SecurityConstants.TOKEN_VALIDITY_DURATION_SECONDS);
        client.setRefreshTokenValiditySeconds(SecurityConstants.REFRESH_TOKEN_VALIDITY_DURATION_SECONDS);
        client.setClientSecret(pwdEncoder.encode(secret));

        clientRegistrationService.addClientDetails(client);

        publisher.publishEvent(new UserVerifiedEvent(client.getClientId(), secret, user.getId(), user.getUsername(), user.getEmail()));

        return user.getId();
    }

    @Override
    public Long setPassword(String username,
                            String password) {
        Objects.requireNonNull(username, "Cannot load user with null username");

        final User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new DbEntryNotFoundException(String.format("User not found by username: '%s' during passwort set", username), User.class);
        }

        user.setPassword(pwdEncoder.encode(password));

        userRepo.save(user);

        return user.getId();
    }

    @Override
    public boolean isPasswordValid(String username,
                                   String password) {
        Objects.requireNonNull(username, "Cannot check user password with null username");
        Objects.requireNonNull(password, "Cannot check user password with null password");

        final User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new DbEntryNotFoundException(String.format("USer not found for username: %s", username), User.class);
        }

        return pwdEncoder.matches(password, user.getPassword());
    }

    @Override
    public UserDto byId(final Long id) {
        Objects.requireNonNull(id, "Cannot load user with null id");

        final User user = userRepo.findOne(id);
        if (user == null) {
            throw new DbEntryNotFoundException(String.format("USer not found for id: %d", id), User.class);
        }

        return mapper.map(user, UserDto.class);
    }

    @Override
    public UserDto byUsername(final String username) {
        Objects.requireNonNull(username, "Cannot load user with null username");

        final User user = userRepo.findByUsername(username);

        return (user != null) ? mapper.map(user, UserDto.class) : null;
    }

    @Override
    public UserDto byVerifyUUID(String uuid) {
        Objects.requireNonNull(uuid, "Cannot load user with null uuid");

        final User user = userRepo.findByVerifyUUID(uuid);
        if (user == null) {
            throw new DbEntryNotFoundException(String.format("USer not found for uuid: %s", uuid), User.class);
        }

        return mapper.map(user, UserDto.class);

    }

    @Override
    public List<UserDto> list() {
        return userRepo.findAll().stream().map(user -> mapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public Long create(final UserDto model) {
        Objects.requireNonNull(model, "Cannot create user for null model");

        User user = mapper.map(model, User.class);
        user.setPassword(UUID.randomUUID().toString());
        user.setVerifyUUID(UUID.randomUUID().toString());
        user.setPasswordValidityDate(LocalDateTime.now().plusMonths(SecurityConstants.PASSWORD_VALIDITY_DURATION_MONTHS));
        if (user.getAdmin()) {
            user.getRoles().add(SecurityConstants.ROLE_ADMIN);
        }
        user.getRoles().add(SecurityConstants.ROLE_CLIENT);

        user = userRepo.save(user);

        publisher.publishEvent(new UserCreatedEvent(user.getEmail(),
                                                    user.getUsername(),
                                                    user.getVerifyUUID()));

        return user.getId();
    }

    @Override
    public Long update(final UserDto model) {
        Objects.requireNonNull(model, "Cannot create user for null model");

        final User user = userRepo.findByUsername(model.getUsername());
        if (user == null) {
            throw new DbEntryNotFoundException(String.format("USer not found for username %s", model.getUsername()), User.class);
        }

        mapper.map(model, user);
        userRepo.save(user);

        return user.getId();
    }

    @Secured(SecurityConstants.ROLE_CLIENT)
    @Override
    public void update(final UserDto model,
                       final String username) {
        Objects.requireNonNull(model, "Cannot update user for null model");
        Objects.requireNonNull(username, "Cannot load user with null username");

        final User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new DbEntryNotFoundException(String.format("USer not found for username %s", username), User.class);
        }

        mapper.map(model, user);
        userRepo.saveAndFlush(user);
    }

    @Override
    public void delete(final String username) {
        Objects.requireNonNull(username, "Cannot load user with null username");

        final User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new DbEntryNotFoundException(String.format("USer not found for username %s", username), User.class);
        }

        user.getClientIds().forEach(clientRegistrationService::removeClientDetails);

        userRepo.delete(user);
    }
}
