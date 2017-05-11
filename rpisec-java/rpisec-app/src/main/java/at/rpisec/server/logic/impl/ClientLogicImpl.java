package at.rpisec.server.logic.impl;

import at.rpisec.server.exception.DbEntryAlreadyExistsException;
import at.rpisec.server.exception.DbEntryNotFoundException;
import at.rpisec.server.jpa.model.Client;
import at.rpisec.server.jpa.repositories.ClientRepository;
import at.rpisec.server.logic.api.ClientLogic;
import at.rpisec.server.shared.rest.constants.SecurityConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@Secured(SecurityConstants.ROLE_CLIENT)
public class ClientLogicImpl implements ClientLogic {

    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private Logger log;

    @Override
    public void checkIfClientExists(final String uuid) {
        Objects.requireNonNull(uuid, "Cannot check if exists with null clientId");

        final Client client = clientRepo.findOne(uuid);
        if (client == null) {
            throw new DbEntryNotFoundException(String.format("Client not found for clientId=%s", uuid), Client.class);
        }
    }

    @Override
    @Secured(SecurityConstants.ROLE_SYSTEM)
    public void register(final String uuid,
                         final Long userId) {
        Client client = clientRepo.findOne(uuid);

        if (client != null) {
            throw new DbEntryAlreadyExistsException("Client already exists exception", Client.class);
        }

        client = new Client();
        client.setId(uuid);
        client.setUserId(userId);

        clientRepo.save(client);
    }

    @Override
    @Secured(SecurityConstants.ROLE_SYSTEM)
    public void unregister(final String uuid) {
        Client client = clientRepo.findOne(uuid);

        if (client == null) {
            throw new DbEntryNotFoundException("Client entry not found", Client.class);
        }

        clientRepo.delete(client.getId());
    }

    @Override
    public void registerFcmToken(final String token,
                                 final String uuid) {
        Client client = clientRepo.findOne(uuid);

        if (client == null) {
            throw new DbEntryNotFoundException("Client entry not found", Client.class);
        }

        client.setFcmToken(token);
        clientRepo.save(client);
    }
}
