package at.rpisec.server.config;

import at.rpisec.server.exception.DbEntryNotFoundException;
import at.rpisec.server.logic.api.UserLogic;
import at.rpisec.server.shared.rest.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/16/17
 */
@Configuration
@Profile("prod")
public class ConfigurationProd {

    @Autowired
    private UserLogic userLogic;

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

                userLogic.create(admin);
                userLogic.create(client);
            }
        };
    }
}
