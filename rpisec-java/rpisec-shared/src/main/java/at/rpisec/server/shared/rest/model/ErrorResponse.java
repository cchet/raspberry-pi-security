package at.rpisec.server.shared.rest.model;

import at.rpisec.server.shared.rest.constants.ResponseErrorCode;
import lombok.Getter;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
public class ErrorResponse {
    @Getter
    private final ResponseErrorCode code;
    @Getter
    private final String message;

    public ErrorResponse(ResponseErrorCode code) {
        this(code, null);
    }

    public ErrorResponse(ResponseErrorCode code,
                         String message) {
        this.code = code;
        this.message = message;
    }
}
