package at.rpisec.server.exception;

import at.rpisec.server.jpa.api.IEntity;
import at.rpisec.server.jpa.model.Client;
import at.rpisec.server.jpa.repositories.ClientRepository;
import at.rpisec.server.shared.rest.constants.ResponseErrorCode;
import at.rpisec.server.shared.rest.model.ErrorResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

/**
 * This class handles the data rest exceptions for the clients.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/15/17
 */
@ControllerAdvice(basePackageClasses = ClientRepository.class)
public class RestExceptionHandler {

    @Autowired
    private Logger logger;

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({DbEntryNotFoundException.class})
    public @ResponseBody
    ErrorResponse handleDbEntryNotFoundError(final DbEntryNotFoundException t) {
        logger.info(String.format("Could not find db entry of type %s", (t.getEntityClass() != null) ? t.getEntityClass().getName() : "unknown"));
        final Class<? extends IEntity> entityClass = t.getEntityClass();
        if (entityClass != null) {
            if (Client.class.equals(entityClass)) {
                return new ErrorResponse(ResponseErrorCode.CLIENT_NOT_FOUND);
            }
        }

        return new ErrorResponse(ResponseErrorCode.DB_ACCESS_ERROR, t.getClass().getName());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({DbEntryAlreadyExistsException.class})
    public @ResponseBody
    ErrorResponse handleDbEntryAlreadyExistsFoundError(final DbEntryAlreadyExistsException t) {
        logger.info(String.format("Db entry already exists of type %s", (t.getEntityClass() != null) ? t.getEntityClass().getName() : "unknown"));
        final Class<? extends IEntity> entityClass = t.getEntityClass();
        if (entityClass != null) {
            if (Client.class.equals(entityClass)) {
                return new ErrorResponse(ResponseErrorCode.CLIENT_ALREADY_REGISTERED);
            }
        }

        return new ErrorResponse(ResponseErrorCode.DB_ACCESS_ERROR, t.getClass().getName());
    }

    @ExceptionHandler({Throwable.class})
    public @ResponseBody
    ErrorResponse defaultHandler(final Throwable t) {
        logger.error("An unknown exception occurred in a RestController ", t);

        return new ErrorResponse(ResponseErrorCode.DB_ACCESS_ERROR, t.getClass().getName());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public @ResponseBody
    ErrorResponse handleConstraintViolation(final ConstraintViolationException e) {
        logger.info("Model validation failed");
        return new ErrorResponse(ResponseErrorCode.VALIDATION_ERROR);
    }
}
