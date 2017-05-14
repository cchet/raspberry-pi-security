package at.rpisec.server.config;

import at.rpisec.server.exception.DbEntryNotFoundException;
import at.rpisec.server.jpa.model.Client;
import at.rpisec.server.jpa.repositories.ClientRepository;
import at.rpisec.server.jpa.repositories.UserRepository;
import at.rpisec.server.logic.api.IncidentLogic;
import at.rpisec.server.logic.api.UserLogic;
import at.rpisec.server.shared.rest.model.UserDto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.tasks.Tasks;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.UUID;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/16/17
 */
@Configuration
@Profile("dev")
public class ConfigurationDev {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private UserLogic userLogic;
    @Autowired
    private IncidentLogic incidentLogic;

    @Autowired
    private FirebaseAuth auth;
    @Autowired
    private Logger log;

    @Bean
    CommandLineRunner produceCommandLineRunner() {
        LocaleContextHolder.setLocale(Locale.US);
        return (args) -> {
            try {
                userLogic.byUsername("admin");
            } catch (DbEntryNotFoundException e) {
                UserDto admin = new UserDto();
                admin.setFirstname("Admin");
                admin.setLastname("Admin");
                admin.setUsername("admin");
                admin.setEmail("philipp.wurm@gmail.com");
                admin.setAdmin(true);

                final UserDto client = new UserDto();
                client.setFirstname("Client_1");
                client.setLastname("Client_1");
                client.setUsername("client");
                client.setEmail("fh.ooe.mus.rpisec@gmail.com");

                final Long adminId = userLogic.create(admin);
                final Long clientId = userLogic.create(client);

                final Client clientAdmin = new Client();
                clientAdmin.setUser(userRepo.findOne(adminId));
                clientAdmin.setUuid(UUID.randomUUID().toString());


                final Client clientClient = new Client();
                clientClient.setUser(userRepo.findOne(clientId));
                clientClient.setUuid(UUID.randomUUID().toString());

                // Info: For testing if notification gets send via FCM
//                Tasks.await(auth.createCustomToken(UUID.randomUUID().toString())
//                                .addOnFailureListener((exception) -> {
//                                    throw new IllegalStateException("Could not retrieve firebase token for dev config");
//                                })
//                                .addOnSuccessListener(clientClient::setFirebaseToken));

                clientRepo.save(clientAdmin);
                clientRepo.save(clientClient);

                log.info("Username: {}, Client-UUID: {}", admin.getUsername(), clientAdmin.getUuid());
                log.info("Username: {}, Client-UUID: {}", client.getUsername(), clientClient.getUuid());
            }

            final byte[] data = IOUtils.toByteArray(ConfigurationDev.class.getResourceAsStream("/giraffe.jpg"));
            incidentLogic.logIncidentWithImage(data, "jpg");
            incidentLogic.logIncidentWithImage(data, "jpg");
            incidentLogic.logIncidentWithImage(data, "jpg");
            incidentLogic.logIncidentWithImage(data, "jpg");
            incidentLogic.logIncidentWithImage(data, "jpg");
            incidentLogic.logIncidentWithImage(data, "jpg");
        };
    }
}
