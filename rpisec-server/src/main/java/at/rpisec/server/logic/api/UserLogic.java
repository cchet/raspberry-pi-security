package at.rpisec.server.logic.api;

import at.rpisec.server.rest.model.UserDto;
import org.springframework.security.core.Authentication;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/17/17
 */
public interface UserLogic extends Logic<UserDto, Long> {

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
}
