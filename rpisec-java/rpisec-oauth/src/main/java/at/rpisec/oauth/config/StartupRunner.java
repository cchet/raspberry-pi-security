package at.rpisec.oauth.config;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private UserDetailsManager userDetailsManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Logger log;

    private static final String ADMIN_USERNAME = "admin";

    @Override
    public void run(String... args) throws Exception {
        if (!userDetailsManager.userExists(ADMIN_USERNAME)) {
            final String password = UUID.randomUUID().toString();
            userDetailsManager.createUser(new User(ADMIN_USERNAME,
                                                   passwordEncoder.encode(password),
                                                   true,
                                                   true,
                                                   true,
                                                   true,
                                                   Arrays.asList(new SimpleGrantedAuthority(SecurityProperties.ROLE_ADMIN), new SimpleGrantedAuthority(SecurityProperties.ROLE_CLIENT))));
            log.info("An administration user has been created with username={} / password={}", ADMIN_USERNAME, password);
        }
    }
}
