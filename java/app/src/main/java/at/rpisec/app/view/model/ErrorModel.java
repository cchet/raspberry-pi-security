package at.rpisec.app.view.model;

import lombok.Value;
import org.springframework.http.HttpStatus;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 06/11/17
 */
@Value
public class ErrorModel {

    private HttpStatus responseStatus;
    private String requestUri;
    private Throwable exception;
}
