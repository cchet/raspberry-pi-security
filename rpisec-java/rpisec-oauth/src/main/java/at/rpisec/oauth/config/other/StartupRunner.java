package at.rpisec.oauth.config.other;

import at.rpisec.oauth.logic.api.ClientDetailsFactory;
import at.rpisec.oauth.logic.api.UserLogic;
import at.rpisec.server.shared.rest.constants.SecurityConstants;
import at.rpisec.server.shared.rest.model.UserDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import java.util.Collections;
import java.util.Objects;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private UserLogic userLogic;
    @Autowired
    private JdbcClientDetailsService clientService;
    @Autowired
    private ConfigProperties.RpisecProperties rpisecProperties;
    @Autowired
    private ApplicationContext appCtx;
    @Autowired
    private Logger log;

    public StartupRunner() {
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Start command line runner: {}", StartupRunner.class.getName());
        if (userLogic.byUsername(SecurityConstants.USER_ADMIN) == null) {
            final String adminEmail = System.getProperty(ConfigProperties.VMOptions.ADMIN_EMAIL);
            Objects.requireNonNull(adminEmail, String.format("If no admin exists then an adminEmail vm argument must be given. Eg.: -D%s=mustermann@gmail.at", ConfigProperties.VMOptions.ADMIN_EMAIL));
            final UserDto admin = new UserDto();
            admin.setFirstname(SecurityConstants.USER_ADMIN);
            admin.setLastname(SecurityConstants.USER_ADMIN);
            admin.setUsername(SecurityConstants.USER_ADMIN);
            admin.setEmail(adminEmail);
            admin.setAdmin(true);

            userLogic.create(admin);
        }

        // Create oauth client for main apps server
        try {
            clientService.loadClientByClientId(rpisecProperties.getClientId());
        } catch (NoSuchClientException e) {
            clientService.addClientDetails(ClientDetailsFactory.createAppClientDetails(rpisecProperties.getClientId(),
                                                                                       rpisecProperties.getClientSecret(),
                                                                                       appCtx.getApplicationName(),
                                                                                       rpisecProperties.getSystemUser(),
                                                                                       Collections.singletonList(rpisecProperties.getResourceId())));
        }
        log.info("Finished command line runner: {}", StartupRunner.class.getName());
    }
}
