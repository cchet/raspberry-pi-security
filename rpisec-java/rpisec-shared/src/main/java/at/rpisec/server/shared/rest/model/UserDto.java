package at.rpisec.server.shared.rest.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
public class UserDto {

    @NotNull
    @Size(max = 100)
    @Getter
    @Setter
    private String firstname;

    @NotNull
    @Size(max = 100)
    @Getter
    @Setter
    private String lastname;

    @NotNull
    @Size(max = 100)
    @Getter
    @Setter
    private String username;

    @NotNull
    @Email
    @Size(max = 100)
    @Getter
    @Setter
    private String email;

    @NotNull
    @Getter
    @Setter
    private boolean admin = false;
}
