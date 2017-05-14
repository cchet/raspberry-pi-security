package at.rpisec.oauth.config.other;

import at.rpisec.oauth.logic.api.UserLogic;
import at.rpisec.server.shared.rest.constants.SecurityConstants;
import at.rpisec.server.shared.rest.model.UserDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import java.util.UUID;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private UserLogic userLogic;

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
    }
}
