package at.rpisec.server.shared.rest.model;

import lombok.Getter;

import java.util.Objects;

/**
 * This class represents the token response for the clients.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
public class TokenResponse {

    @Getter
    private final String created;

    @Getter
    private final String token;

    @Getter
    private final String error;

    public TokenResponse(String created,
                         String token,
                         String error) {
        Objects.requireNonNull(created, "Created date time must not be null");
        Objects.requireNonNull(token, "Token must not be null");

        this.created = created;
        this.token = token;
        this.error = error;
    }
}
