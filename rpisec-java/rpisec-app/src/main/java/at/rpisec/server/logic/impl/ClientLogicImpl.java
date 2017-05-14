package at.rpisec.server.logic.impl;

import at.rpisec.server.exception.DbEntryNotFoundException;
import at.rpisec.server.jpa.model.Client;
import at.rpisec.server.jpa.repositories.ClientRepository;
import at.rpisec.server.logic.api.ClientLogic;
import at.rpisec.server.shared.rest.constants.SecurityConstants;
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

    @Override
    public void registerFcmToken(final String token,
                                 final String uuid) {
        Objects.requireNonNull(uuid, "Cannot register fcm token for null client id");
        Objects.requireNonNull(token, "Cannot register null fcm token");

        Client client = clientRepo.findOne(uuid);

        if (client == null) {
            throw new DbEntryNotFoundException("Client entry not found", Client.class);
        }

        client.setFcmToken(token);
        clientRepo.save(client);
    }
}
