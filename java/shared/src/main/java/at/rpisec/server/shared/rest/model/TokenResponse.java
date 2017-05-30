package at.rpisec.server.shared.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the token response for the clients.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    private String created;
    private String token;
    private String clientId;
    private String clientSecret;
}
