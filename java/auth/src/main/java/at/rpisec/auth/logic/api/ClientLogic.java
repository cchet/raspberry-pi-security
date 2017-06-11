package at.rpisec.auth.logic.api;


import at.rpisec.auth.rest.model.TokenResponse;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 05/17/17
 */
public interface ClientLogic {

    TokenResponse loginClient(String username,
                              String deviceId);

    void registerFcmToken(String username,
                          String deviceId,
                          String fcmToken);
}
