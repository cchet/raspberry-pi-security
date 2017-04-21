package at.rpisec.server.logic.event.observer;

import at.rpisec.server.logic.event.UserCreatedEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
@Component
public class UserEventObserver {

    @Autowired
    private MailSender sender;
    @Autowired
    private Logger log;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void observeUserCreatedEvent(final UserCreatedEvent event) {
        log.info("User Verification email sent\nname: " + event.getFullname() + "\nemail: " + event.getEmail() + "\nuuid: " + event.getUuid());
    }
}
