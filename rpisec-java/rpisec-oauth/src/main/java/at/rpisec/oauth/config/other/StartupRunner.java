package at.rpisec.oauth.config.other;

import at.rpisec.oauth.logic.api.UserLogic;
import at.rpisec.server.shared.rest.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private UserLogic userLogic;

    public static final String ADMIN_USERNAME = "admin";

    @Override
    public void run(String... args) throws Exception {
        if (userLogic.byUsername(ADMIN_USERNAME) == null) {
            UserDto admin = new UserDto();
            admin.setFirstname("Admin");
            admin.setLastname("Admin");
            admin.setUsername("admin");
            admin.setEmail("herzog.thomas81@gmail.com");
            admin.setAdmin(true);

            final Long adminId = userLogic.create(admin);
        }
    }
}
