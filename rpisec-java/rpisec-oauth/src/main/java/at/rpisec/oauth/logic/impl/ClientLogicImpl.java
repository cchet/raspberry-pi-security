package at.rpisec.oauth.logic.impl;

import at.rpisec.oauth.config.SecurityConfiguration;
import at.rpisec.oauth.exception.DbEntryNotFoundException;
import at.rpisec.oauth.jpa.model.User;
import at.rpisec.oauth.jpa.repositories.UserRepository;
import at.rpisec.oauth.logic.api.ClientDetailsFactory;
import at.rpisec.oauth.logic.api.ClientLogic;
import at.rpisec.oauth.logic.event.ClientCreatedEvent;
import at.rpisec.oauth.logic.event.ClientRemovedEvent;
import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.constants.SecurityConstants;
import at.rpisec.server.shared.rest.model.TokenResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.Tasks;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 05/17/17
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@Secured(SecurityConstants.ROLE_CLIENT)
public class ClientLogicImpl implements ClientLogic {

    @Autowired
    @Qualifier(SecurityConfiguration.QUALIFIER_APP_SERVER_RESOURCE_ID)
    private String resourceId;
    @Autowired
    private ClientRegistrationService clientRegistrationService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private ApplicationContext appCtx;
    @Autowired
    private FirebaseAuth firebaseAuth;
    @Autowired
    private Logger log;

    @Override
    public TokenResponse loginClient(final String username,
                                     final String deviceId) {
        Objects.requireNonNull(username, "Cannot load user with null uuid");
        Objects.requireNonNull(deviceId, "Cannot register client for null deviceId");

        final User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new DbEntryNotFoundException(String.format("User not found by username: '%s' during verify", username), User.class);
        }

        final String clientId = UUID.randomUUID().toString();
        final String secret = UUID.randomUUID().toString();

        final String oldClientId = user.getClientIds().get(deviceId.toUpperCase());
        if (oldClientId != null) {
            try {
                clientRegistrationService.removeClientDetails(oldClientId);
            } catch (NoSuchClientException e) {
                log.warn("Client id could not be deleted. client_id={}", oldClientId);
            }
            publisher.publishEvent(new ClientRemovedEvent(oldClientId));
        }
        user.getClientIds().put(deviceId.toUpperCase(), clientId);
        userRepo.saveAndFlush(user);

        final ClientDetails client = ClientDetailsFactory.createMobileClientDetails(clientId,
                                                                                    secret,
                                                                                    appCtx.getApplicationName(),
                                                                                    user.getUsername(),
                                                                                    "Default Device",
                                                                                    Collections.singletonList(resourceId));
        clientRegistrationService.addClientDetails(client);

        publisher.publishEvent(new ClientCreatedEvent(client.getClientId(), user.getId()));

        final Task<String> task = firebaseAuth.createCustomToken(UUID.randomUUID().toString());
        String firebaseToken;
        try {
            firebaseToken = Tasks.await(task);
        } catch (Exception e) {
            throw new IllegalStateException("Could not retrieve firebase token");
        }

        return new TokenResponse(LocalDateTime.now().format(DateTimeFormatter.ofPattern(ClientRestConstants.PATTERN_DATE_TIME)),
                                 firebaseToken,
                                 clientId,
                                 secret);
    }
}
