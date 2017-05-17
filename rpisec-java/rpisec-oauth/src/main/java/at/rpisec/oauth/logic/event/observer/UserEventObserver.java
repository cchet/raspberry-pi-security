package at.rpisec.oauth.logic.event.observer;

import at.rpisec.oauth.logic.event.UserCreatedEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Locale;

/**
 * Observer class for observing user related events.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
@Component
public class UserEventObserver {

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
}
