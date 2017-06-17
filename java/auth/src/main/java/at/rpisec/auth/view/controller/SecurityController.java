package at.rpisec.auth.view.controller;

import at.rpisec.auth.logic.api.UserLogic;
import at.rpisec.auth.view.model.PasswordChangeFormModel;
import at.rpisec.auth.view.model.VerifyAccountFormModel;
import at.rpisec.shared.rest.model.UserDto;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * The view controller for the security related views.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
@Controller
@RequestMapping("/")
public class SecurityController {

    @Autowired
    private UserLogic userLogic;

    @GetMapping("/verifyAccount")
    @Validated
    public ModelAndView verifyAccount(final @NotEmpty @RequestParam(value = "uuid") String uuid) {
        final UserDto user = userLogic.byVerifyUUID(uuid);

        return (user != null)
                ? new ModelAndView("verify_account", "model", new VerifyAccountFormModel(uuid))
                : new ModelAndView("404");
    }

    @PostMapping("/verifyAccount")
    public ModelAndView verifyAccount(final @Valid @ModelAttribute("model") VerifyAccountFormModel model,
                                      final BindingResult result) {
        if (result.hasFieldErrors()) {
            return new ModelAndView("verify_account", "model", model);
        }

        if (!model.getPassword().equals(model.getConfirmPassword())) {
            result.addError(new FieldError("model", "password", null, false, new String[]{"unequal.passwords"}, null, "unknown"));
            return new ModelAndView("verify_account", "model", model);
        }

        userLogic.verifyAccount(model.getUuid(), model.getPassword());

        return new ModelAndView("verify_account_success");
    }

    @GetMapping("/changePassword")
    @Validated
    public ModelAndView setPassword(final @NotEmpty @RequestParam(value = "username") String username) {
        final UserDto user = userLogic.byUsername(username);
        return new ModelAndView("change_password", "model", new PasswordChangeFormModel(user.getUsername()));
    }

    @PostMapping("/changePassword")
    @Validated
    public ModelAndView setPassword(final @Valid @ModelAttribute("model") PasswordChangeFormModel model,
                                    final BindingResult result) {
        if (result.hasFieldErrors()) {
            return new ModelAndView("change_password", "model", model);
        }

        if (!model.getPassword().equals(model.getConfirmPassword())) {
            result.addError(new FieldError("model", "password", null, false, new String[]{"unequal.passwords"}, null, "unknown"));
            return new ModelAndView("change_password", "model", model);
        }

        if (!userLogic.isPasswordValid(model.getUsername(), model.getOldPassword())) {
            result.addError(new FieldError("model", "oldPassword", null, false, new String[]{"invalid.password"}, null, "unknown"));
            return new ModelAndView("change_password", "model", model);
        }

        userLogic.setPassword(model.getUsername(), model.getPassword());

        return new ModelAndView("change_password_success");
    }
}
