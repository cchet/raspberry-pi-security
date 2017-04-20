package at.rpisec.server.shared.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This class represents the token response for the clients.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/19/17
 */
public class TokenResponse {

    @Getter
    @JsonFormat(pattern = "dd.MM.yyyy hh:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime created;

    @Getter
    private final String token;

    @Getter
    private final String error;

    public TokenResponse(String token) {
        this(LocalDateTime.now(), token, null);
    }

    public TokenResponse(String token,
                         String error) {
        this(LocalDateTime.now(), token, error);
    }

    public TokenResponse(LocalDateTime created,
                         String token,
                         String error) {
        Objects.requireNonNull(created, "Created date time must not be null");
        Objects.requireNonNull(token, "Token must not be null");

        this.created = created;
        this.token = token;
        this.error = error;
    }
}
