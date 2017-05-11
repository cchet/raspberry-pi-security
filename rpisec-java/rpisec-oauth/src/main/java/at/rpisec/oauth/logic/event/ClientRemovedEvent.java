package at.rpisec.oauth.logic.event;

import lombok.Value;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
@Value
public final class ClientRemovedEvent {

    private String clientId;
    private String username;
    private String email;
}
