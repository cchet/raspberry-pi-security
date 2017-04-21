package at.rpisec.server.logic.impl;

import at.rpisec.server.logic.api.IncidentLogic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class IncidentLogicImpl implements IncidentLogic {
}
