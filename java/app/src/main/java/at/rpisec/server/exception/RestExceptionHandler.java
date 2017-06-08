package at.rpisec.server.exception;

import at.rpisec.server.rest.ClientRestController;
import at.rpisec.server.shared.rest.model.ErrorResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

/**
 * This class handles the data rest exceptions for the clients.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/15/17
 */
@ControllerAdvice(basePackageClasses = ClientRestController.class)
public class RestExceptionHandler {

    @Autowired
    private Logger logger;

    @ExceptionHandler({ConstraintViolationException.class, MissingServletRequestParameterException.class})
    public @ResponseBody ResponseEntity<ErrorResponse> handleMissingParameterError(final Exception exception) {
        final String message = exception.getMessage();
        logger.info("Validation failed for rest call parameter or model. message: {}", message);
        return new ResponseEntity<>(new ErrorResponse(String.format("Rest call failed for invalid model or request parameter. message: %s", message)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DbEntryNotFoundException.class})
    public @ResponseBody ResponseEntity<ErrorResponse> handleDbEntryNotFoundError(final DbEntryNotFoundException t) {
        logger.info(String.format("Could not find db entry of type %s", t.getEntityClass().getName()));

        return new ResponseEntity<>(new ErrorResponse("Could not find db entry"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DbEntryAlreadyExistsException.class})
    public @ResponseBody ResponseEntity<ErrorResponse> handleDbEntryAlreadyExistsFoundError(final DbEntryAlreadyExistsException t) {
        logger.info(String.format("Db entry already exists of type %s", t.getEntityClass().getName()));

        return new ResponseEntity<>(new ErrorResponse(String.format("An entry of type '%s' already exists.", t.getEntityClass().getName())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Throwable.class})
    public @ResponseBody ResponseEntity<ErrorResponse> defaultHandler(final Throwable t) {
        logger.error("An unknown exception occurred in a RestController ", t);

        return new ResponseEntity<>(new ErrorResponse("An unhandled exception occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
