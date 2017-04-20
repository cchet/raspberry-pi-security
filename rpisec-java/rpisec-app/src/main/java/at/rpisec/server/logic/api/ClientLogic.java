package at.rpisec.server.logic.api;

import at.rpisec.server.shared.rest.model.UserDto;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/20/17
 */
public interface ClientLogic {

    Long register(String uuid,
                  String username);

    void unregister(String uuid,
                    String username);

    Long updateProfile(String uuid,
                       String username,
                       UserDto model);

    Long updatePassword(String uuid,
                        String username,
                        String password);
}
