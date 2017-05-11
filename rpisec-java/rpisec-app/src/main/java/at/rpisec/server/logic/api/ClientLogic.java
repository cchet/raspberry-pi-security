package at.rpisec.server.logic.api;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
public interface ClientLogic {

    void checkIfClientExists(String uuid);

    void register(String uuid,
                  Long userId);

    void unregister(String uuid);

    void registerFcmToken(String token,
                          String uuid);
}
