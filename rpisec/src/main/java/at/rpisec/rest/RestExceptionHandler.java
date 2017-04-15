package at.rpisec.rest;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This class handles the data rest exceptions for the clients.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/15/17
 */
@ControllerAdvice(basePackageClasses = RepositoryRestExceptionHandler.class)
public class RestExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(){
        // TODO: Log error and refine error message
        return new ResponseEntity<>("{\"error\": \"Failed\"}", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
