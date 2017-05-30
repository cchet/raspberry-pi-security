package at.rpisec.oauth.logic.event;

import lombok.Value;

import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
@Value
public final class ClientRemovedEvent {

    private List<String> deviceIds;
    private Long userId;
}
