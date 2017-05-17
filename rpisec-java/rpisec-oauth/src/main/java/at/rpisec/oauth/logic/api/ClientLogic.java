package at.rpisec.oauth.logic.api;

import at.rpisec.server.shared.rest.model.TokenResponse;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 05/17/17
 */
public interface ClientLogic {

    TokenResponse loginClient(String username,
                              String deviceId);
}
