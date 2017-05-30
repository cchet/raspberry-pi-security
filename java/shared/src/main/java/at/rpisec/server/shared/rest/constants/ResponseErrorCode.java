package at.rpisec.server.shared.rest.constants;

/**
 * This enumeration specifies the rest response errors which indicate the server logic error to the client.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
public enum ResponseErrorCode {

    /**
     * Db access error which indicates an error with the communication with the database and or services.
     */
    DB_ACCESS_ERROR,
    /**
     * Auth error which indicates a error with the authentication server
     */
    AUTH_ERROR,
    /**
     * Error if the user could not be found
     */
    USER_NOT_FOUND,
    /**
     * Validation error which indicates an error with the request data
     */
    VALIDATION_ERROR,
    /**
     * Error which tells that the client could nto be found
     */
    CLIENT_NOT_FOUND,
    /**
     * Error if the client has been already registered to the user
     */
    CLIENT_ALREADY_REGISTERED;
}
