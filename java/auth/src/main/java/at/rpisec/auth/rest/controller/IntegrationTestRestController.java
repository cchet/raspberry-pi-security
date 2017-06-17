package at.rpisec.auth.rest.controller;

import at.rpisec.auth.config.other.ConfigProperties;
import at.rpisec.auth.jpa.model.ClientDevice;
import at.rpisec.auth.jpa.model.User;
import at.rpisec.auth.jpa.repositories.UserRepository;
import at.rpisec.auth.logic.api.ClientDetailsFactory;
import at.rpisec.shared.rest.constants.AppRestConstants;
import at.rpisec.shared.rest.constants.SecurityConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;

/**
 * This class represents the rest interface for the integration tests which is only active if the 'integrationTest' profile is active.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/05/17
 */
@RestController
@RequestMapping("/test")
@ConditionalOnProperty(name = "test.integration.rest.api.enabled", havingValue = "true")
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
    private Logger log;

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
        admin.setVerifyUUID(null);
        admin.setAdmin(true);
        admin.getRoles().add(SecurityConstants.ROLE_ADMIN);
        admin.getRoles().add(SecurityConstants.ROLE_CLIENT);
        admin.getClientDevices().put(deviceId, new ClientDevice(SecurityConstants.USER_ADMIN));
        userRepo.save(admin);

        // Create default client device
        clientDetailsService.addClientDetails(ClientDetailsFactory.createMobileClientDetails(deviceId,
                                                                                             deviceId,
                                                                                             "rpisec-test-auth",
                                                                                             SecurityConstants.USER_ADMIN,
                                                                                             "Default mobile device",
                                                                                             Collections.singletonList(rpisecProperties.getResourceId())));

        // Create oauth client for main apps server
        clientDetailsService.addClientDetails(ClientDetailsFactory.createAppClientDetails(rpisecProperties.getClientId(),
                                                                                          rpisecProperties.getClientSecret(),
                                                                                          "rpisec-test-auth",
                                                                                          rpisecProperties.getSystemUser(),
                                                                                          Collections.singletonList(rpisecProperties.getResourceId())));

        // register client on app server
        final MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.put(AppRestConstants.PARAM_DEVICE_ID, Collections.singletonList(deviceId));
        data.put(AppRestConstants.PARAM_USER_ID, Collections.singletonList(admin.getId().toString()));
        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(data, new HttpHeaders() {{
            put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        }});

        createRestTemplate().postForEntity(rpisecProperties.getBaseUrl() + "/test/prepare", entity, Void.class);
    }

    private RestTemplate createRestTemplate() {
        final RestTemplate appRestTemplate = new RestTemplate();
        final String auth = rpisecProperties.getSystemUser() + ":" + rpisecProperties.getSystemPassword();
        final String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(Charset.forName("US-ASCII")));

        appRestTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        appRestTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().put(HttpHeaders.AUTHORIZATION, Collections.singletonList(String.format("Basic %s", encodedAuth)));
            return execution.execute(request, body);
        });

        appRestTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return !HttpStatus.OK.equals(response.getStatusCode());
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                log.error("Could not post oauth client to app server");
            }
        });

        return appRestTemplate;
    }
}
