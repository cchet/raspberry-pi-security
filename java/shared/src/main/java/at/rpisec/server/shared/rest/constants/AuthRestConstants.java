package at.rpisec.server.shared.rest.constants;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/10/17
 */
public class AuthRestConstants {

    public static final String PATTERN_DATE_TIME = "dd.MM.yyyy hh:mm:ss";

    //region Client rest constants
    public static final String CLIENT_REST_API_BASE = "/api/client";
    public static final String REL_CLIENT_LOGIN = "/clientLogin";
    public static final String REL_URI_REGISTER_FCM_TOKEN = "/registerFcmToken";

    /**
     * The uri for the 'GET' request to retrieve the authentication data (firebase_token, client_id and client_secret).<br>
     * <p>
     * Requires the request params:<ul>
     * <li>{@link AuthRestConstants#PARAM_DEVICE_ID}</li>
     * </ul>
     * This api requires the a registered user authentication.<br>
     */
    public static final String URI_CLIENT_LOGIN = CLIENT_REST_API_BASE + REL_CLIENT_LOGIN;

    /**
     * The uri for the 'PUT' request to register the client firebase cloud messaging token.
     * Requires the request param {@link AuthRestConstants#PARAM_FCM_TOKEN}
     */
    public static final String URI_REGISTER_FCM_TOKEN = CLIENT_REST_API_BASE + REL_URI_REGISTER_FCM_TOKEN;
    //endregion

    //region User rest constants
    public static final String USER_REST_PAI_BASE = "/api/user";
    public static final String REL_URI_LIST = "/list";
    public static final String REL_URI_GET = "/get";
    public static final String REL_URI_CREATE = "/create";
    public static final String REL_URI_UPDATE = "/update";
    public static final String REL_URI_DELETE = "/delete";

    /**
     * URI for the 'GET' request to list all users.<br>
     * User must have admin privileges
     */
    public static final String URI_LIST = USER_REST_PAI_BASE + REL_URI_LIST;

    /**
     * URI for the 'GET' request to get a single user by its id.<br>
     * User must have admin privileges.<br>
     * The path must have the username as a suffix '/get/{username}'.
     */
    public static final String URI_GET = USER_REST_PAI_BASE + REL_URI_GET;

    /**
     * URI for the 'PUT' request to create a new user.<br>
     * User must have admin privileges.<br>
     * The request body must contain a valid instance of the model {@link at.rpisec.server.shared.rest.model.UserDto}
     */
    public static final String URI_CREATE = USER_REST_PAI_BASE + REL_URI_CREATE;

    /**
     * URI for the 'POST' request to update a user identified by its id.<br>
     * User must have admin privileges.<br>
     * The request body must contain a valid instance of the model {@link at.rpisec.server.shared.rest.model.UserDto}
     */
    public static final String URI_UPDATE = USER_REST_PAI_BASE + REL_URI_UPDATE;

    /**
     * URI for the 'DELETE' request to delete a user identified by its id.<br>
     * User must have admin privileges.<br>
     * The path must have the username as a suffix '/delete/{username}'.
     */
    public static final String URI_DELETE = USER_REST_PAI_BASE + REL_URI_DELETE;

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
    //endregion

    //region OAuth rest constants
    public static final String OAUTH2_REST_PAI_BASE = "/oauth";
    public static final String REL_URI_TOKEN = "/token";
    public static final String REL_URI_TOKEN_CHECK = "/check_token";

    // dynamic params
    public static final String REL_URI_TOKEN_CHECK_PARAMS = "?token=%s";

    // static params
    public static final String REL_URI_TOKEN_PARAMS = "?grant_type=password&scope=write";

    // oauth-server-get-access-token
    public static final String URI_TOKEN_REQUEST = OAUTH2_REST_PAI_BASE + REL_URI_TOKEN + REL_URI_TOKEN_PARAMS;

    // oauth-server-check-access-token
    public static final String URI_TOKEN_CHECK = OAUTH2_REST_PAI_BASE + REL_URI_TOKEN_CHECK + REL_URI_TOKEN_CHECK_PARAMS;
    //endregion
}
