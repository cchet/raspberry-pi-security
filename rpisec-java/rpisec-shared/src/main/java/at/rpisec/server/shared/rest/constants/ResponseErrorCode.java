package at.rpisec.server.shared.rest.constants;

/**
 * This enumeration specifies the rest response errors which indicate the server logic error to the client.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
public enum ResponseErrorCode {

    /**
     * Generic error if not concrete error has been handled
     */
    DB_ACCESS_ERROR,
    /**
     * Error if the bean validation failed on the submitted json model
     */
    VALIDATION_ERROR,
    /**
     * Error if the client has not been found on the database
     */
    CLIENT_NOT_FOUND,
    /**
     * Error if the user could not be found on the database
     */
    USER_NOT_FOUND,
    /**
     * Error if the client has been already registered to the user
     */
    CLIENT_ALREADY_REGISTERED;
}
