package at.rpisec.server.view.controller;

import at.rpisec.server.logic.api.UserLogic;
import at.rpisec.server.shared.rest.model.UserDto;
import at.rpisec.server.view.model.PasswordChangeFormModel;
import at.rpisec.server.view.model.VerifyAccountFormModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
@Controller
public class SecurityController {

    @Autowired
    private UserLogic userLogic;

    @GetMapping("/verifyAccount")
    public ModelAndView verifyAccount(final @RequestParam(value = "uuid") String uuid) {
        final UserDto user = userLogic.byVerifyUUID(uuid);

        return new ModelAndView("verify_account", "model", new VerifyAccountFormModel(uuid));
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
    public ModelAndView setPassword(final @RequestParam(value = "username") String username) {
        final UserDto user = userLogic.byUsername(username);
        return new ModelAndView("change_password", "model", new PasswordChangeFormModel(user.getUsername()));
    }

    @PostMapping("/changePassword")
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
