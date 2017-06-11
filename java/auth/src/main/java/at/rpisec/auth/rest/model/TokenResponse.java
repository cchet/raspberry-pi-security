package at.rpisec.auth.rest.model;

import at.rpisec.shared.rest.model.ErrorResponse;
import lombok.*;

/**
 * This class represents the token response for the clients.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse extends ErrorResponse {

    private String created;
    private String token;
    private String clientId;
    private String clientSecret;
}
