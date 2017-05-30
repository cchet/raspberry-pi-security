package at.rpisec.oauth.logic.event;

import lombok.Value;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
@Value
public final class ClientFcmTokenRegisteredEvent {

    private String deviceId;
    private String fcmToken;
    private Long userId;
}
