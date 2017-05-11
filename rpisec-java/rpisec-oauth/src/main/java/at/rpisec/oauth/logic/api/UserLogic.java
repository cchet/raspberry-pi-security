package at.rpisec.oauth.logic.api;

import at.rpisec.server.shared.rest.model.UserDto;

import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/17/17
 */
public interface UserLogic {

    /**
     * Verifies the user email address and enables the account if not verified yet.
     *
     * @param uuid     the uuid identifying the user account
     * @param password the user's password
     * @return the user id
     * @throws NullPointerException                               if the uuid or the raw password is null
     * @throws at.rpisec.oauth.exception.DbEntryNotFoundException if the user could not be found for the given uuid
     */
    Long verifyAccount(String uuid,
                       String password);

    /**
     * Sets a new password for the given user.
     *
     * @param username the username of the account to set new password for
     * @param password the user's password
     * @return the user id
     * @throws NullPointerException                               if the username or the raw password is null
     * @throws at.rpisec.oauth.exception.DbEntryNotFoundException if the user could not be found for the given username
     */
    Long setPassword(String username,
                     String password);

    /**
     * Answers the question if the given password matches the saved user one.
     *
     * @param username the user's username
     * @param password the raw password
     * @return true if valid, false otherwise
     * @throws NullPointerException                               if the username or the raw password is null
     * @throws at.rpisec.oauth.exception.DbEntryNotFoundException if the user could not be found for the given username
     */
    boolean isPasswordValid(String username,
                            String password);


    UserDto byId(Long id);

    UserDto byUsername(String username);

    UserDto byVerifyUUID(String uuid);

    List<UserDto> list();

    Long create(UserDto model);

    Long update(UserDto model);

    void update(UserDto model,
                String username);

    void delete(String username);
}
