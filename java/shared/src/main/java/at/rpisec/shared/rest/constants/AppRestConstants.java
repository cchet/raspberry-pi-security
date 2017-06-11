package at.rpisec.shared.rest.constants;

/**
 * This class specifies the client rest api and related constants.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
public class AppRestConstants {

    private AppRestConstants() {
    }

    public static final String PATTERN_DATE_TIME = "dd.MM.yyyy hh:mm:ss";

    public static final String INTERNAL_REST_API_BASE = "/internal";
    public static final String REL_URI_REGISTER = "/register";
    public static final String REL_URI_UNREGISTER = "/unregister";
    public static final String REL_URI_REGISTER_FCM_TOKEN = "/registerFcmToken";

    public static final String CLIENT_REST_API_BASE = "/api/client";
    public static final String REL_URI_CAPTURE = "/capture";


    /**
     * The uri for the 'PUT' request to register a client.<br>
     * This api accepts the media type 'application/x-www-form-urlencoded'<br>
     * Parameters are <ul>
     * <li>{@link AppRestConstants#PARAM_DEVICE_ID}</li>
     * <li>{@link AppRestConstants#PARAM_USER_ID}</li>
     * </ul>
     * This api requires the system user authentication of the app server.<br>
     * This api returns http status 200 or an http status != 200 in case of an error
     */
    public static final String URI_REGISTER = INTERNAL_REST_API_BASE + REL_URI_REGISTER;

    /**
     * The uri for the 'DELETE' request to unregister a client.
     * This api accepts the media type 'application/x-www-form-urlencoded'<br>
     * Parameters are <ul>
     * <li>{@link AppRestConstants#PARAM_DEVICE_ID}</li>
     * <li>{@link AppRestConstants#PARAM_USER_ID}</li>
     * </ul>
     * This api requires the system user authentication of the app server.<br>
     * This api returns http status 200 or an http status != 200 in case of an error
     */
    public static final String URI_UNREGISTER = INTERNAL_REST_API_BASE + REL_URI_UNREGISTER;

    /**
     * The uri for the 'PUT' request to register the client firebase cloud messaging token.
     * Parameters are <ul>
     * <li>{@link AppRestConstants#PARAM_DEVICE_ID}</li>
     * <li>{@link AppRestConstants#PARAM_USER_ID}</li>
     * </ul>
     * This api requires the system user authentication of the app server.<br>
     * This api returns http status 200 or an http status != 200 in case of an error
     */
    public static final String URI_REGISTER_FCM_TOKEN = INTERNAL_REST_API_BASE + REL_URI_REGISTER_FCM_TOKEN;

    /**
     * The uri for the 'GET' request to get a camera capture.
     * <p>
     * This api requires a oauth authentication of the current user
     */
    public static final String URI_CAPTURE = CLIENT_REST_API_BASE + REL_URI_CAPTURE;
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
