package at.rpisec.server.logic.api;

import at.rpisec.server.shared.rest.model.UserDto;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/17/17
 */
public interface UserLogic {

    UserDto byId(Long id);

    UserDto byUsername(String username);

    UserDto byVerifyUUID(String uuid);

    List<UserDto> list();

    Long create(UserDto model);

    Long update(UserDto model);

    void update(UserDto model,
                String username);

    void delete(String username);

    /**
     * Logs the user in
     *
     * @param username    the user's username
     * @param rawPassword the user's raw password
     * @return the authentication instance
     * @throws NullPointerException                                                if the username or raw password is null
     * @throws org.springframework.security.authentication.BadCredentialsException if either the user cannot be found or the password is invalid
     */
    Authentication login(String username,
                         String rawPassword);

    /**
     * Verifies the user email address and enables the account if not verified yet.
     *
     * @param username the username of the account to verify
     * @param uuid     the uuid identifying the user account
     * @param password the user's password
     * @return the user id
     */
    Long verifyAccount(String username,
                       String uuid,
                       String password);
    /**
     * Sets a new password for the given user.
     *
     * @param username the username of the account to set new password for
     * @param password the user's password
     * @return the user id
     */
    Long setPassword(String username,
                       String password);
}
