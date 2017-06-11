package at.rpisec.app.logic.event.observer;

import at.rpisec.app.logic.event.IncidentOccurredEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
@Component
public class IncidentEventObserver {

    @Autowired
    private Logger log;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    void observeIncidentOccuredEvent(final IncidentOccurredEvent event) {
        log.info("#observeIncidentOccuredEvent called");
    }

}
