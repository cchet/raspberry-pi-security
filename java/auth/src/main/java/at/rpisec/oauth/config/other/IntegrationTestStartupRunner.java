package at.rpisec.oauth.config.other;

import at.rpisec.oauth.logic.api.ClientDetailsFactory;
import at.rpisec.oauth.logic.api.UserLogic;
import at.rpisec.server.shared.rest.constants.SecurityConstants;
import at.rpisec.server.shared.rest.model.UserDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import java.util.Collections;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
public class IntegrationTestStartupRunner implements CommandLineRunner {

    @Autowired
    private UserLogic userLogic;
    @Autowired
    private JdbcClientDetailsService clientService;
    @Autowired
    private ConfigProperties.RpisecProperties rpisecProperties;

    @Override
    public void run(String... args) throws Exception {
        final String adminEmail = "fh.ooe.mus.rpisec@gmail.com";
        final UserDto admin = new UserDto();
        admin.setFirstname(SecurityConstants.USER_ADMIN);
        admin.setLastname(SecurityConstants.USER_ADMIN);
        admin.setUsername(SecurityConstants.USER_ADMIN);
        admin.setEmail(adminEmail);
        admin.setAdmin(true);
        userLogic.create(admin);

        // Create oauth client for main apps server
        clientService.addClientDetails(ClientDetailsFactory.createAppClientDetails(rpisecProperties.getClientId(),
                                                                                   rpisecProperties.getClientSecret(),
                                                                                   "rpisec-auth",
                                                                                   rpisecProperties.getSystemUser(),
                                                                                   Collections.singletonList(rpisecProperties.getResourceId())));
    }
}
