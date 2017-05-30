package at.rpisec.server.shared.rest.model;

import at.rpisec.server.shared.rest.constants.ResponseErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */

@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse {

    private ResponseErrorCode code;

    private String message;

    public ErrorResponse(ResponseErrorCode code) {
        this(code, null);
    }

    public ErrorResponse(ResponseErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }
}
