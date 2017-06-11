package at.rpisec.oauth.exception.handler;

import at.rpisec.server.shared.rest.model.ErrorResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/11/17
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private Logger logger;

    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleNoHandlerFound(final NoHandlerFoundException ex) {
        final String requestUrl = ex.getRequestURL();
        // response with json model
        if (requestUrl.toUpperCase().contains("/api/".toUpperCase())) {
            logger.info("Rest api not found. requestUrl: ", requestUrl);
            return new ResponseEntity<>(new ErrorResponse(String.format("RestAPi not found. url: %s", requestUrl)), HttpStatus.NOT_FOUND);
        }
        // response with error page
        else {
            logger.info("View not found. requestUrl: ", requestUrl);
            return new ModelAndView("404");
        }
    }
}
