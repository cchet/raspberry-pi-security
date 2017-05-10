package at.rpisec.server.logic.api;

import at.rpisec.server.shared.rest.model.UserDto;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
public interface ClientLogic {

    void checkIfClientExists(String uuid,
                             String username);

    Long register(String uuid,
                  String username);

    void unregister(String uuid,
                    String username);

    void registerFcmToken(String token, String uuid, String username);

    Long updateProfile(String uuid,
                       String username,
                       UserDto model);

    Long updatePassword(String uuid,
                        String username,
                        String password);
}
