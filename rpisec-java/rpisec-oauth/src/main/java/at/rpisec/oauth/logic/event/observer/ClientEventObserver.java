package at.rpisec.oauth.logic.event.observer;

import at.rpisec.oauth.config.SecurityConfiguration;
import at.rpisec.oauth.logic.event.ClientCreatedEvent;
import at.rpisec.oauth.logic.event.ClientFcmTokenRegisteredEvent;
import at.rpisec.oauth.logic.event.ClientRemovedEvent;
import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Observer class for observing client related events.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/11/17
 */
@Component
public class ClientEventObserver {

    @Autowired
    @Qualifier(SecurityConfiguration.QUALIFIER_OAUTH_URL_UNREGISTER_CLIENT)
    private String urlUnregisterClient;
    @Autowired
    @Qualifier(SecurityConfiguration.QUALIFIER_OAUTH_URL_REGISTER_CLIENT)
    private String urlRegisterClient;
    @Autowired
    @Qualifier(SecurityConfiguration.QUALIFIER_OAUTH_URL_REGISTER_CLIENT_FCM_TOKEN)
    private String urlRegisterClientFcmToken;
    @Autowired
    @Qualifier(SecurityConfiguration.QUALIFIER_OAUTH_REST_TEMPLATE)
    private RestTemplate appRestTemplate;

    @Autowired
    private Logger log;

    /**
     * Unregisters the created client device on the app server via its hosted rest api.
     *
     * @param event the event holding the request data
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void observerAfterCommitClientRemovedEvent(final ClientRemovedEvent event) {

        final MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.put(ClientRestConstants.PARAM_DEVICE_ID, event.getDeviceIds());
        data.put(ClientRestConstants.PARAM_USER_ID, Collections.singletonList(event.getUserId().toString()));
        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(data, new HttpHeaders() {{
            put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        }});

        appRestTemplate.postForEntity(urlUnregisterClient, entity, Void.class);

        log.info("Unregistered client device on app server. deviceIds={}", String.join(",", event.getDeviceIds()));
    }

    /**
     * Registers the created client device on the app server via its hosted rest api.
     *
     * @param event the event holding the request data
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void observerAfterCommitClientCreatedEvent(final ClientCreatedEvent event) {

        final Map<String, List<Object>> data = new LinkedMultiValueMap<>();
        data.put(ClientRestConstants.PARAM_DEVICE_ID, Collections.singletonList(event.getDeviceId()));
        data.put(ClientRestConstants.PARAM_USER_ID, Collections.singletonList(event.getUserId().toString()));
        final HttpEntity<Map<String, List<Object>>> entity = new HttpEntity<>(data, new HttpHeaders() {{
            put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        }});

        appRestTemplate.postForEntity(urlRegisterClient, entity, Void.class);

        log.info("Registered client device on app server. user_id={} / client_id={}", event.getUserId(), event.getDeviceId());
    }

    /**
     * Registers the client device fcm token on the app server via its hosted rest api.
     *
     * @param event the event holding the request data
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void observerAfterCommitClientFcmTokenRegisteredEvent(final ClientFcmTokenRegisteredEvent event) {

        final Map<String, List<Object>> data = new LinkedMultiValueMap<>();
        data.put(ClientRestConstants.PARAM_DEVICE_ID, Collections.singletonList(event.getDeviceId()));
        data.put(ClientRestConstants.PARAM_FCM_TOKEN, Collections.singletonList(event.getFcmToken()));
        data.put(ClientRestConstants.PARAM_USER_ID, Collections.singletonList(event.getUserId().toString()));
        final HttpEntity<Map<String, List<Object>>> entity = new HttpEntity<>(data, new HttpHeaders() {{
            put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        }});

        appRestTemplate.postForEntity(urlRegisterClientFcmToken, entity, Void.class);

        log.info("Registered client device fcm token on app server. user_id={} / client_id={}", event.getUserId(), event.getDeviceId());
    }
}
