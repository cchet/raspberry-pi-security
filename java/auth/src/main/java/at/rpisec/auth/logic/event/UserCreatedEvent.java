package at.rpisec.auth.logic.event;

import lombok.Value;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
@Value
public final class UserCreatedEvent {

    private String email;
    private String username;
    private String uuid;
}
