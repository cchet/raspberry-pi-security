package at.rpisec.server.logic.api;

import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
public interface IClientLogic {

    void register(String deviceId,
                  Long userId);

    void unregister(List<String> deviceIds, Long userId);

    void registerFcmToken(String token,
                          String deviceId,
                          Long userId);
}
