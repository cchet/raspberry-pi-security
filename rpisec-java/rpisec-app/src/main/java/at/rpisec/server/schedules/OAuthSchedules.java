package at.rpisec.server.schedules;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/11/17
 */
@Component
public class OAuthSchedules {

    @Autowired
    private Logger log;

    @Scheduled(fixedRate = (5 * 1000))
    public void syncClientUsers() {
        log.info("Sync client schedule called");
    }
}
