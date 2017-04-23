package at.rpisec.server.view.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/22/17
 */
@NoArgsConstructor
public class PasswordChangeFormModel {

    @NotEmpty
    @Getter
    @Setter
    private String username;

    @NotEmpty
    @Getter
    @Setter
    private String oldPassword;

    @Size(min = 8, max = 100)
    @Getter
    @Setter
    private String password;

    @Size(min = 8, max = 100)
    @Getter
    @Setter
    private String confirmPassword;

    public PasswordChangeFormModel(String username) {
        this.username = username;
    }
}
