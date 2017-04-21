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
    private final String fullname;
    @Getter
    private final String uuid;

    public UserCreatedEvent(String email,
                            String fullname,
                            String uuid) {
        this.email = email;
        this.fullname = fullname;
        this.uuid = uuid;
    }
}
