package at.rpisec.server.view.controller;

import at.rpisec.server.logic.api.UserLogic;
import at.rpisec.server.shared.rest.model.UserDto;
import at.rpisec.server.view.model.PasswordFormModel;
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
public class VerificationRestController {

    @Autowired
    private UserLogic userLogic;

    @GetMapping("/verifyAccount")
    public ModelAndView verifyAccount(final @RequestParam(value = "uuid") String uuid) {
        final UserDto user = userLogic.byVerifyUUID(uuid);

        return new ModelAndView("verify_account", "model", new PasswordFormModel(user.getUsername()));
    }

    @PostMapping("/verifySetPassword")
    public ModelAndView verifyAccount(final @Valid @ModelAttribute("model") PasswordFormModel model,
                                      final BindingResult result) {
        if (result.hasFieldErrors()) {
            return new ModelAndView("verify_account", "model", model);
        }

        if (!model.getPassword().equals(model.getConfirmPassword())) {
            result.addError(new FieldError("model", "password", null, false, new String[]{"unequal.passwords"}, null, "unknown"));
            return new ModelAndView("verify_account", "model", model);
        }

        return new ModelAndView("verified_account");
    }
}
