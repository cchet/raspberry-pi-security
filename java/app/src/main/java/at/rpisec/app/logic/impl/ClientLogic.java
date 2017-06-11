package at.rpisec.app.logic.impl;

import at.rpisec.app.jpa.model.ClientId;
import at.rpisec.app.exception.DbEntryNotFoundException;
import at.rpisec.app.jpa.model.Client;
import at.rpisec.app.jpa.repositories.ClientRepository;
import at.rpisec.app.logic.api.IClientLogic;
import at.rpisec.shared.rest.constants.SecurityConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@Secured(SecurityConstants.ROLE_SYSTEM)
public class ClientLogic implements IClientLogic {

    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private Logger log;

    @Override
    public void register(final String deviceId,
                         final Long userId) {
        final ClientId id = new ClientId(deviceId, userId);
        Client client = clientRepo.findOne(id);

        if (client == null) {
            client = new Client();
            client.setId(id);
            clientRepo.save(client);
        } else {
            log.warn("Client device already registered. deviceId={} / userId={}", deviceId, userId);
        }

    }

    @Override
    public void unregister(final List<String> deviceIds,
                           final Long userId) {
        deviceIds.stream().map(deviceId -> new ClientId(deviceId, userId)).forEach(clientRepo::delete);
    }

    @Override
    public void registerFcmToken(final String token,
                                 final String deviceId,
                                 final Long userId) {
        Objects.requireNonNull(deviceId, "Cannot register fcm token for null client id");
        Objects.requireNonNull(token, "Cannot register null fcm token");

        Client client = clientRepo.findOne(new ClientId(deviceId, userId));

        if (client == null) {
            throw new DbEntryNotFoundException("Client entry not found", Client.class);
        }

        client.setFcmToken(token);
        clientRepo.save(client);
    }
}
