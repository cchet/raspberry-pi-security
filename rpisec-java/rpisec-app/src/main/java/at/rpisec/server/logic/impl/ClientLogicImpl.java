package at.rpisec.server.logic.impl;

import at.rpisec.server.config.SecurityProperties;
import at.rpisec.server.exception.DbEntryAlreadyExistsException;
import at.rpisec.server.exception.DbEntryNotFoundException;
import at.rpisec.server.jpa.model.Client;
import at.rpisec.server.jpa.repositories.ClientRepository;
import at.rpisec.server.logic.api.ClientLogic;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@Secured(SecurityProperties.ROLE_CLIENT)
public class ClientLogicImpl implements ClientLogic {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private MapperFacade mapper;


    @Override
    public void checkIfClientExists(String uuid) {
        Objects.requireNonNull(uuid, "Cannot check if exists with null uuid");

        final Client client = clientRepo.findByUuid(uuid);
        if (client == null) {
            throw new DbEntryNotFoundException(String.format("Client not found for uuid=%s", uuid), Client.class);
        }
    }

    @Override
    public Long register(final String uuid) {
        Client client = clientRepo.findByUuid(uuid);

        if (client != null) {
            throw new DbEntryAlreadyExistsException("Client already exists exception", Client.class);
        }

        client = new Client();
        client.setUuid(uuid);

        return clientRepo.save(client).getId();
    }

    @Override
    public void unregister(String uuid) {
        Client client = clientRepo.findByUuid(uuid);

        if (client == null) {
            throw new DbEntryNotFoundException("Client entry not found", Client.class);
        }

        clientRepo.delete(client.getId());
    }

    @Override
    public void registerFcmToken(String token,
                                 String uuid) {
        Client client = clientRepo.findByUuid(uuid);

        if (client == null) {
            throw new DbEntryNotFoundException("Client entry not found", Client.class);
        }

        client.setFcmToken(token);
        clientRepo.save(client);
    }
}
