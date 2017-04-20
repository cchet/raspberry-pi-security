package at.rpisec.server.logic.impl;

import at.rpisec.server.config.SecurityProperties;
import at.rpisec.server.exception.DbEntryAlreadyExistsException;
import at.rpisec.server.exception.DbEntryNotFoundException;
import at.rpisec.server.jpa.model.Client;
import at.rpisec.server.jpa.model.User;
import at.rpisec.server.jpa.repositories.ClientRepository;
import at.rpisec.server.jpa.repositories.UserRepository;
import at.rpisec.server.logic.api.ClientLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/20/17
 */
@Service
@Transactional
@Secured(SecurityProperties.ROLE_CLIENT)
public class ClientLogicImpl implements ClientLogic {

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public Long create(final String uuid,
                       final String username) {
        final User user = userRepo.findByUsername(username);
        Client client = clientRepo.findByUuidAndUser(uuid, user);

        if (client != null) {
            throw new DbEntryAlreadyExistsException("Client already exists exception", Client.class);
        }

        client = new Client();
        client.setUuid(uuid);
        client.setUser(user);

        return clientRepo.save(client).getId();
    }

    @Override
    public void delete(String uuid,
                       String username) {
        final User user = userRepo.findByUsername(username);
        Client client = clientRepo.findByUuidAndUser(uuid, user);

        if (client == null) {
            throw new DbEntryNotFoundException("Client entry not found", Client.class);
        }

        clientRepo.delete(client.getId());
    }
}
