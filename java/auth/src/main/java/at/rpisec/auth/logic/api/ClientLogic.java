package at.rpisec.auth.logic.api;


import at.rpisec.auth.rest.model.TokenResponse;

/**
 * This interface specifics the client related business logic.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 05/17/17
 */
public interface ClientLogic {

    /**
     * Logs in a user via its client device. An oauth2 client is created for this device id and an already
     * registered client for this device id will be deleted. Teh client will always get a new clientId and clientSecret.
     *
     * @param username the username hwo wants to login
     * @param deviceId the id of the user's client device
     * @return the token response for the client app.
     * @throws NullPointerException  if one parameter is null
     * @throws IllegalStateException if the firebase token cannot be retrieved
     */
    TokenResponse loginClient(String username,
                              String deviceId);

    /**
     * Registers the fcm token of a client device.
     *
     * @param username the user's username
     * @param deviceId the id of the client device
     * @param fcmToken the fcm token to register
     * @throws NullPointerException                              if one parameter is null
     * @throws at.rpisec.auth.exception.DbEntryNotFoundException if the user could not be found
     */
    void registerFcmToken(String username,
                          String deviceId,
                          String fcmToken);
}
