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
    public static final String REL_URI_UPDATE = "/update";

    /**
     * The uri for a token request for the firebase login.
     * Requires the request param {@link ClientRestConstants#PARAM_UUID}
     */
    public static final String URI_TOKEN = BASE_URI + REL_URI_TOKEN;

    /**
     * The uri for the client app registration.
     * Requires the request param {@link ClientRestConstants#PARAM_UUID}
     * If returns status 409, then the attribute code on the response json will hold the error inormation
     *
     * @see ResponseErrorCode the enumeration holding the error codes
     */
    public static final String URI_REGISTER = BASE_URI + REL_URI_REGISTER;

    /**
     * URI for updating a client user.<br>
     * The request body must contain a valid instance of the model {@link at.rpisec.server.shared.rest.model.UserDto}
     */
    public static final String URI_UPDATE = BASE_URI + REL_URI_UPDATE;

    /**
     * The request parameter for identifying the client uuid at a get token request.
     */
    public static final String PARAM_UUID = "uuid";

}
