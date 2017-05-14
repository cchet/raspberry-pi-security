package at.rpisec.oauth.config.other;

import at.rpisec.oauth.logic.api.ClientDetailsFactory;
import at.rpisec.oauth.logic.api.UserLogic;
import at.rpisec.server.shared.rest.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import java.util.Collections;

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

    private final boolean productive;

    public static final String ADMIN_USERNAME = "admin";

    public StartupRunner(boolean productive) {
        this.productive = productive;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userLogic.byUsername(ADMIN_USERNAME) == null) {
            UserDto admin = new UserDto();
            admin.setFirstname("Admin");
            admin.setLastname("Admin");
            admin.setUsername("admin");
            admin.setEmail("herzog.thomas81@gmail.com");
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
    }
}
