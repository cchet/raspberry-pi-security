package at.rpisec.server.logic.event;

import lombok.Getter;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
public final class UserCreatedEvent {
    @Getter
    private final String email;
    @Getter
    private final String username;
    @Getter
    private final String uuid;

    public UserCreatedEvent(String email,
                            String username,
                            String uuid) {
        this.email = email;
        this.username = username;
        this.uuid = uuid;
    }
}
