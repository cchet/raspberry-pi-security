package at.rpisec.oauth.rest;

import at.rpisec.oauth.config.SecurityConfiguration;
import at.rpisec.oauth.config.other.ConfigProperties;
import at.rpisec.oauth.jpa.model.ClientDevice;
import at.rpisec.oauth.jpa.model.User;
import at.rpisec.oauth.jpa.repositories.UserRepository;
import at.rpisec.oauth.logic.api.ClientDetailsFactory;
import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.constants.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/05/17
 */
@RestController
@RequestMapping("/api/system")
@ConditionalOnProperty(name = "system.api.alive.enabled", havingValue = "true")
public class IntegrationTestRestController {

    @Autowired
    private JdbcClientDetailsService clientDetailsService;
    @Autowired
    private ConfigProperties.RpisecProperties rpisecProperties;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    @Qualifier(SecurityConfiguration.QUALIFIER_OAUTH_REST_TEMPLATE)
    private RestTemplate appRestTemplate;

    @GetMapping("/alive")
    public boolean alive() {
        return true;
    }

    @PostMapping("/prepare")
    public void clear() {
        // clear oauth clients
        clientDetailsService.listClientDetails().stream()
                            .map(ClientDetails::getClientId)
                            .forEach(clientDetailsService::removeClientDetails);

        // clear all user
        userRepo.deleteAll();

        // create default data
        final String deviceId = SecurityConstants.ADMIN;
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
        admin.getClientDevices().put(deviceId, new ClientDevice(SecurityConstants.USER_ADMIN));
        userRepo.save(admin);

        // Create default client device
        clientDetailsService.addClientDetails(ClientDetailsFactory.createMobileClientDetails(SecurityConstants.USER_ADMIN,
                                                                                             SecurityConstants.USER_ADMIN,
                                                                                             "rpisec-test-auth",
                                                                                             SecurityConstants.USER_ADMIN,
                                                                                             deviceId,
                                                                                             Collections.singletonList(rpisecProperties.getResourceId())));

        // Create oauth client for main apps server
        clientDetailsService.addClientDetails(ClientDetailsFactory.createAppClientDetails(rpisecProperties.getClientId(),
                                                                                          rpisecProperties.getClientSecret(),
                                                                                          "rpisec-test-auth",
                                                                                          rpisecProperties.getSystemUser(),
                                                                                          Collections.singletonList(rpisecProperties.getResourceId())));

        // register client on app server
        final MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.put(ClientRestConstants.PARAM_DEVICE_ID, Collections.singletonList(deviceId));
        data.put(ClientRestConstants.PARAM_USER_ID, Collections.singletonList(admin.getId().toString()));
        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(data, new HttpHeaders() {{
            put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        }});

        appRestTemplate.postForEntity(rpisecProperties.getBaseUrl() + "/system/prepare", entity, Void.class);
    }
}
