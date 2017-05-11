package at.rpisec.oauth.logic.event.observer;

import at.rpisec.oauth.config.SecurityConfiguration;
import at.rpisec.oauth.logic.event.UserCreatedEvent;
import at.rpisec.oauth.logic.event.UserVerifiedEvent;
import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Observer class for observing user related events.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
@Component
public class UserEventObserver {

    @Autowired
    @Qualifier(SecurityConfiguration.QUALIFIER_OAUTH_REST_TEMPLATE)
    private RestTemplate appRestTemplate;
    @Autowired
    @Qualifier(SecurityConfiguration.QUALIFIER_OAUTH_URL_REGISTER_CLIENT)
    private String urlRegisterClient;

    @Autowired
    private MessageSource messages;
    @Autowired()
    @Qualifier("baseUrl")
    private String serverRootUrl;
    @Value("spring.mail.username")
    private String from;
    @Autowired
    private MailSender sender;
    @Autowired
    private Logger log;

    /**
     * Creates and sends an email to the created user for the account verification.
     *
     * @param event the user created event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void observeUserCreatedEvent(final UserCreatedEvent event) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String url = serverRootUrl + "/verifyAccount?uuid=" + event.getUuid();
        final String subject = messages.getMessage("email.user.created.subject", null, locale);
        final String content = messages.getMessage("email.user.created.content", new String[]{event.getUsername(), url}, locale);
        final SimpleMailMessage msg = new SimpleMailMessage();
        msg.setSubject(subject);
        msg.setText(content);
        msg.setTo(event.getEmail());

        sender.send(msg);

        log.info("User Verification email sent for username: {} / email: {}", event.getUsername(), event.getEmail());
    }

    /**
     * Registers the created oauth client on the app server via its hosted rest api.
     *
     * @param event the user verified event
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void observerBeforeCommitUserVerifiedEvent(final UserVerifiedEvent event) {

        final Map<String, List<Object>> data = new LinkedMultiValueMap<>();
        data.put(ClientRestConstants.PARAM_UUID, Collections.singletonList(event.getClientId()));
        data.put(ClientRestConstants.PARAM_USER_ID, Collections.singletonList(event.getUserId().toString()));
        final HttpEntity<Map<String, List<Object>>> entity = new HttpEntity<>(data, new HttpHeaders() {{
            put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        }});

        appRestTemplate.postForEntity(urlRegisterClient, entity, Void.class);

        log.info("Registered oauth client on app server. user_id={} / client_id={}", event.getUserId(), event.getClientId());
    }

    /**
     * Creates and sends an email to the user about the successfully performed account verification and newly created client with the client_id and client_secret.
     *
     * @param event the user verified event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void observeAfterCommitUserVerifiedEvent(final UserVerifiedEvent event) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String subject = messages.getMessage("email.account.verified.subject", null, locale);
        final String content = messages.getMessage("email.account.verified.content", new String[]{event.getUsername(), event.getClientId(), event.getClientSecret()}, locale);
        final SimpleMailMessage msg = new SimpleMailMessage();
        msg.setSubject(subject);
        msg.setText(content);
        msg.setTo(event.getEmail());

        sender.send(msg);

        log.info("Account verified email sent for username={} / email={} / client_id={}", event.getUsername(), event.getEmail(), event.getClientId());
    }
}
