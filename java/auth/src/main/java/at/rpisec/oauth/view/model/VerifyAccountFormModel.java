package at.rpisec.oauth.view.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/22/17
 */
@NoArgsConstructor
@Getter
@Setter
public class VerifyAccountFormModel {

    private String uuid;

    @Size(min = 8, max = 100)
    private String password;

    @Size(min = 8, max = 100)
    private String confirmPassword;

    public VerifyAccountFormModel(String uuid) {
        this.uuid = uuid;
    }
}
