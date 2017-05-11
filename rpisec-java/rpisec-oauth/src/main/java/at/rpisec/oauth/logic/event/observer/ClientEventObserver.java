package at.rpisec.oauth.logic.event.observer;

import at.rpisec.oauth.config.SecurityConfiguration;
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
    @Qualifier(SecurityConfiguration.QUALIFIER_OAUTH_REST_TEMPLATE)
    private RestTemplate appRestTemplate;
    @Autowired
    @Qualifier(SecurityConfiguration.QUALIFIER_OAUTH_URL_UNREGISTER_CLIENT)
    private String urlUnregisterClient;

    @Autowired
    private Logger log;

    /**
     * Unregisters the created oauth client on the app server via its hosted rest api.
     *
     * @param event the client removed event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void observerAfterCommitClientRemovedEvent(final ClientRemovedEvent event) {

        final Map<String, List<Object>> data = new LinkedMultiValueMap<>();
        data.put(ClientRestConstants.PARAM_UUID, Collections.singletonList(event.getClientId()));
        final HttpEntity<Map<String, List<Object>>> entity = new HttpEntity<>(data, new HttpHeaders() {{
            put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        }});

        appRestTemplate.postForEntity(urlUnregisterClient, entity, Void.class);

        log.info("Unregistered oauth client on app server. client_id={}", event.getClientId());
    }
}
