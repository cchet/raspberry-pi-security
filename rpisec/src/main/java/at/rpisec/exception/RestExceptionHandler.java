package at.rpisec.exception;

import lombok.Getter;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class handles the data rest exceptions for the clients.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/15/17
 */
@ControllerAdvice(basePackageClasses = RepositoryRestExceptionHandler.class)
public class RestExceptionHandler {

    @Autowired
    private Logger logger;

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({HibernateException.class, DataAccessException.class})
    public @ResponseBody SimpleExceptionResponseModel handleInternalServerDbError(final Throwable t) {
        logger.error("Internal error caused by database error occurred", t);
        return new SimpleExceptionResponseModel("Internal Server on db access occurred", t.getClass().getName());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public @ResponseBody List<BeanValidationResponseModel> handleConstraintViolation(final ConstraintViolationException e) {
        logger.debug("Bad request caused by Constraint validation error occurred", e);
        return e.getConstraintViolations().stream().map(violation -> new BeanValidationResponseModel(violation.getPropertyPath()
                                                                                                              .toString(), violation.getMessage(), violation
                                                                                                             .getInvalidValue())).collect(Collectors.toList());
    }

    private static final class SimpleExceptionResponseModel {
        @Getter
        private final String message;
        @Getter
        private final String exception;

        SimpleExceptionResponseModel(String message,
                                     String exception) {
            this.message = message;
            this.exception = exception;
        }
    }

    private static final class BeanValidationResponseModel {
        @Getter
        private final String field;
        @Getter
        private final String message;
        @Getter
        private final Object invalidValue;

        BeanValidationResponseModel(String field,
                                    String message,
                                    Object invalidValue) {
            this.field = field;
            this.message = message;
            this.invalidValue = invalidValue;
        }
    }
}
