package at.rpisec.rest.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/19/17
 */
public class UserDto {

    @Getter
    @Setter
    private Long id;

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
    @Size(max = 500)
    @Getter
    @Setter
    private String password;

    @NotNull
    @Email
    @Size(max = 100)
    @Getter
    @Setter
    private String email;

    @NotNull
    @Getter
    @Setter
    private Boolean admin;
}
