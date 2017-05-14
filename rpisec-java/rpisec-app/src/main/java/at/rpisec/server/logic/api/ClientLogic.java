package at.rpisec.server.logic.api;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
public interface ClientLogic {

    void registerFcmToken(String token,
                          String uuid);
}
