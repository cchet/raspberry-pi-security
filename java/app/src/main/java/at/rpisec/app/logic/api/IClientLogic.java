package at.rpisec.app.logic.api;

import java.util.List;

/**
 * This interface specifies the client related business logic.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
public interface IClientLogic {

    /**
     * Registers a client device for the given user
     *
     * @param deviceId teh device id
     * @param userId   the user id to assign device to
     * @throws NullPointerException     if one parameter is null
     * @throws IllegalArgumentException if the device id is an empty string
     */
    void register(String deviceId,
                  Long userId);

    /**
     * Unregister the assigned client device.
     *
     * @param deviceIds the device id
     * @param userId    the user id the device is assigned to
     * @throws NullPointerException if one parameter is null
     */
    void unregister(List<String> deviceIds,
                    Long userId);

    /**
     * Register the fcm token for the client device.
     *
     * @param token    the fcm token
     * @param deviceId the device id
     * @param userId   the user di the device is assigned to
     * @throws NullPointerException     if one parameter is null
     * @throws IllegalArgumentException if the device id is an empty string
     */
    void registerFcmToken(String token,
                          String deviceId,
                          Long userId);
}
