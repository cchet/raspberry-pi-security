package at.rpisec.oauth.config.other;

import at.rpisec.oauth.jpa.model.ClientDevice;
import at.rpisec.oauth.jpa.model.User;
import at.rpisec.oauth.jpa.repositories.UserRepository;
import at.rpisec.oauth.logic.api.ClientDetailsFactory;
import at.rpisec.server.shared.rest.constants.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
public class IntegrationTestStartupRunner implements CommandLineRunner {

    @Autowired
    private JdbcClientDetailsService clientService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ConfigProperties.RpisecProperties rpisecProperties;

    @Override
    public void run(String... args) throws Exception {
        // create test admin user
        final String adminEmail = "fh.ooe.mus.rpisec@gmail.com";
        final User admin = new User();
        admin.setFirstname(SecurityConstants.USER_ADMIN);
        admin.setLastname(SecurityConstants.USER_ADMIN);
        admin.setUsername(SecurityConstants.USER_ADMIN);
        admin.setEmail(adminEmail);
        admin.setPassword(encoder.encode(SecurityConstants.USER_ADMIN));
        admin.setPasswordValidityDate(LocalDateTime.now().plusYears(100));
        admin.setVerifiedAt(LocalDateTime.now());
        admin.setAdmin(true);
        admin.getClientDevices().put("device", new ClientDevice(SecurityConstants.USER_ADMIN));
        userRepo.save(admin);

        // Create default client device
        clientService.addClientDetails(ClientDetailsFactory.createMobileClientDetails(SecurityConstants.USER_ADMIN,
                                                                                      SecurityConstants.USER_ADMIN,
                                                                                      "rpisec-test-auth",
                                                                                      SecurityConstants.USER_ADMIN,
                                                                                      "Default Device",
                                                                                      Collections.singletonList(rpisecProperties.getResourceId())));

        // Create oauth client for main apps server
        clientService.addClientDetails(ClientDetailsFactory.createAppClientDetails(rpisecProperties.getClientId(),
                                                                                   rpisecProperties.getClientSecret(),
                                                                                   "rpisec-test-auth",
                                                                                   rpisecProperties.getSystemUser(),
                                                                                   Collections.singletonList(rpisecProperties.getResourceId())));
    }
}
