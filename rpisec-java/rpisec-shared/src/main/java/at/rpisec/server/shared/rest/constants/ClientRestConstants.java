package at.rpisec.server.shared.rest.constants;

/**
 * This class specifies the client available request parameter.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/20/17
 */
public class ClientRestConstants {

    private ClientRestConstants() {
    }

    public static final String BASE_URI = "/api/client";
    public static final String REL_URI_TOKEN = "/token";
    public static final String REL_URI_REGISTER = "/register";
    public static final String REL_URI_UNREGISTER = "/unregister";
    public static final String REL_URI_PROFILE = "/profile";
    public static final String REL_URI_PASSWORD = "/password";

    /**
     * The uri for the 'GET' request to retrieve a firebase token.
     * Requires the request param {@link ClientRestConstants#PARAM_UUID}
     */
    public static final String URI_TOKEN = BASE_URI + REL_URI_TOKEN;

    /**
     * The uri for the 'PUT' request to register a client.
     * Requires the request param {@link ClientRestConstants#PARAM_UUID}
     * If returns status 409, then the attribute code on the response json will hold the error inormation
     *
     * @see ResponseErrorCode the enumeration holding the error codes
     */
    public static final String URI_REGISTER = BASE_URI + REL_URI_REGISTER;

    /**
     * URI for the 'POST' request to update the client user related profile.<br>
     * The request body must contain a valid instance of the model {@link at.rpisec.server.shared.rest.model.UserDto}<br>
     * The path must have the client uuid as a suffix '/update/{uuid}'
     */
    public static final String URI_PROFILE = BASE_URI + REL_URI_PROFILE;

    /**
     * URI for the 'POST' request to update the client user password.<br>
     * Requires the request param {@link ClientRestConstants#PARAM_UUID}
     * Requires the request param {@link ClientRestConstants#PARAM_PASSWORD}<br>
     * The path must have the client uuid as a suffix '/update/{uuid}'
     */
    public static final String URI_PASSWORD = BASE_URI + REL_URI_PASSWORD;

    /**
     * The request parameter for identifying the client uuid.
     */
    public static final String PARAM_UUID = "uuid";
    /**
     * The request parameter for identifying the client password.
     */
    public static final String PARAM_PASSWORD = "password";

}
