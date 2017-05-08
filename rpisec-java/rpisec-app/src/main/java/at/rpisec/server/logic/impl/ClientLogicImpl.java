package at.rpisec.server.logic.impl;

import at.rpisec.server.config.SecurityProperties;
import at.rpisec.server.exception.DbEntryAlreadyExistsException;
import at.rpisec.server.exception.DbEntryNotFoundException;
import at.rpisec.server.jpa.model.Client;
import at.rpisec.server.jpa.model.User;
import at.rpisec.server.jpa.repositories.ClientRepository;
import at.rpisec.server.jpa.repositories.UserRepository;
import at.rpisec.server.logic.api.ClientLogic;
import at.rpisec.server.shared.rest.model.UserDto;
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
    private UserRepository userRepo;
    @Autowired
    private MapperFacade mapper;


    @Override
    public void checkIfClientExists(String uuid,
                                    String username) {
        Objects.requireNonNull(uuid, "Cannot check if exists with null uuid");
        Objects.requireNonNull(username, "Cannot check if exists with null username");

        final User user = userRepo.findByUsernameAndVerifyDateNotNull(username);
        if (user == null) {
            throw new DbEntryNotFoundException(String.format("User not found for id %s", username), User.class);
        }
        final Client client = clientRepo.findByUuidAndUser(uuid, user);
        if (client == null) {
            throw new DbEntryNotFoundException(String.format("Client not found for username=%s and uuid=%s", username, uuid), Client.class);
        }
    }

    @Override
    public Long register(final String uuid,
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
    public void unregister(String uuid,
                           String username) {
        final User user = userRepo.findByUsername(username);
        Client client = clientRepo.findByUuidAndUser(uuid, user);

        if (client == null) {
            throw new DbEntryNotFoundException("Client entry not found", Client.class);
        }

        clientRepo.delete(client.getId());
    }

    @Override
    public void registerFcmToken(String token,
                                 String uuid,
                                 String username) {
        final User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new DbEntryNotFoundException("User not found", User.class);
        }
        Client client = clientRepo.findByUuidAndUser(uuid, user);

        if (client == null) {
            throw new DbEntryNotFoundException("Client entry not found", Client.class);
        }

        client.setFcmToken(token);
        clientRepo.save(client);
    }

    @Override
    public Long updateProfile(String uuid,
                              String username,
                              UserDto model) {
        final User user = userRepo.findByUsernameAndClientUuid(username, uuid);
        if (user == null) {
            throw new DbEntryNotFoundException("No user for username and uuid", User.class);
        }

        mapper.map(model, user);
        userRepo.save(user);

        return user.getId();
    }

    @Override
    public Long updatePassword(String uuid,
                               String username,
                               String password) {
        final User user = userRepo.findByUsernameAndClientUuid(username, uuid);
        if (user == null) {
            throw new DbEntryNotFoundException("No user for username and uuid", User.class);
        }

        user.setPassword(encoder.encode(password));
        userRepo.save(user);

        return user.getId();
    }
}
