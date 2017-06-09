package at.rpisec.server.shared.rest.constants;

/**
 * This class specifies the client rest api and related constants.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
public class ClientRestConstants {

    private ClientRestConstants() {
    }

    public static final String PATTERN_DATE_TIME = "dd.MM.yyyy hh:mm:ss";

    public static final String BASE_URI = "/api/client";
    public static final String BASE_INTERNAL_URI = "/internal";
    public static final String REL_URI_REGISTER = "/register";
    public static final String REL_URI_UNREGISTER = "/unregister";
    public static final String REL_CLIENT_LOGIN = "/clientLogin";
    public static final String REL_URI_REGISTER_FCM_TOKEN = "/registerFcmToken";


    /**
     * The uri for the 'PUT' request to register a client.<br>
     * This api accepts the media type 'application/x-www-form-urlencoded'<br>
     * Form parameters are <ul>
     * <li>{@link ClientRestConstants#PARAM_DEVICE_ID}</li>
     * <li>{@link ClientRestConstants#PARAM_USER_ID}</li>
     * </ul>
     * This api requires the system user authentication of the app server.<br>
     * This api returns http status 200 or an http status != 200 in case of an error
     *
     */
    public static final String URI_REGISTER = BASE_INTERNAL_URI + REL_URI_REGISTER;

    /**
     * The uri for the 'DELETE' request to unregister a client.
     * This api accepts the media type 'application/x-www-form-urlencoded'<br>
     * Form parameters are <ul>
     * <li>{@link ClientRestConstants#PARAM_DEVICE_ID}</li>
     * </ul>
     * This api requires the system user authentication of the app server.<br>
     * This api returns http status 200 or an http status != 200 in case of an error
     */
    public static final String URI_UNREGISTER = BASE_INTERNAL_URI + REL_URI_UNREGISTER;

    /**
     * The uri for the 'GET' request to retrieve the authentication data (firebase_token, client_id and client_secret).<br>
     * <p>
     * Requires the request params:<ul>
     * <li>{@link ClientRestConstants#PARAM_DEVICE_ID}</li>
     * </ul>
     * This api requires the a registered user authentication.<br>
     */
    public static final String URI_CLIENT_LOGIN = BASE_URI + REL_CLIENT_LOGIN;

    /**
     * The uri for the 'PUT' request to register the client firebase cloud messaging token.
     * Requires the request param {@link ClientRestConstants#PARAM_FCM_TOKEN}
     */
    public static final String URI_REGISTER_FCM_TOKEN = BASE_INTERNAL_URI + REL_URI_REGISTER_FCM_TOKEN;

    /**
     * The request parameter for the device id.
     */
    public static final String PARAM_DEVICE_ID = "deviceId";

    /**
     * The request parameter for the user id.
     */
    public static final String PARAM_USER_ID = "userId";

    /**
     * The request parameter for the firebase cloud messaging token.
     */
    public static final String PARAM_FCM_TOKEN = "fcmToken";

}
