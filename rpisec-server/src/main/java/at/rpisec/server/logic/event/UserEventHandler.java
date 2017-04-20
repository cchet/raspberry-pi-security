package at.rpisec.server.logic.event;

import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/20/17
 */
@Component
public class UserEventHandler {

    @Autowired
    private MailSender sender;
    @Autowired
    private Logger log;

    public static final class UserCreatedEvent {
        @Getter
        private final String email;
        @Getter
        private final String fullname;
        @Getter
        private final String uuid;

        public UserCreatedEvent(String email,
                                String fullname,
                                String uuid) {
            this.email = email;
            this.fullname = fullname;
            this.uuid = uuid;
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handleUserCreatedEvent(final UserCreatedEvent event) {
        log.info("User Verification email sent\nname: " + event.getFullname() + "\nemail: " + event.getEmail() + "\nuuid: " + event.getUuid());
    }
}
