package at.rpisec.server.shared.rest.constants;

/**
 * This class specifies the client available request parameter.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/20/17
 */
public class UserRestConstants {

    private UserRestConstants() {
    }

    public static final String REST_BASE = "/api/user";
    public static final String REL_URI_LIST = "/list";
    public static final String REL_URI_GET = "/get";
    public static final String REL_URI_CREATE = "/create";
    public static final String REL_URI_UPDATE = "/update";
    public static final String REL_URI_DELETE = "/delete";

    /**
     * URI for the 'GET' request to list all users.<br>
     * User must have admin privileges
     */
    public static final String URI_LIST = REST_BASE + REL_URI_LIST;

    /**
     * URI for the 'GET' request to get a single user by its id.<br>
     * User must have admin privileges.<br>
     * The path must have the username as a suffix '/get/{username}'.
     */
    public static final String URI_GET = REST_BASE + REL_URI_GET;

    /**
     * URI for the 'PUT' request to create a new user.<br>
     * User must have admin privileges.<br>
     * The request body must contain a valid instance of the model {@link at.rpisec.server.shared.rest.model.UserDto}
     */
    public static final String URI_CREATE = REST_BASE + REL_URI_CREATE;

    /**
     * URI for the 'POST' request to update a user identified by its id.<br>
     * User must have admin privileges.<br>
     * The request body must contain a valid instance of the model {@link at.rpisec.server.shared.rest.model.UserDto}
     */
    public static final String URI_UPDATE = REST_BASE + REL_URI_UPDATE;

    /**
     * URI for the 'DELETE' request to delete a user identified by its id.<br>
     * User must have admin privileges.<br>
     * The path must have the username as a suffix '/delete/{username}'.
     */
    public static final String URI_DELETE = REST_BASE + REL_URI_DELETE;
}
