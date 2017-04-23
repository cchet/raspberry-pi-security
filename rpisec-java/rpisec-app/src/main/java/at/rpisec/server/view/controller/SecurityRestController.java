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
import java.util.HashMap;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
@Controller
public class SecurityRestController {

    @Autowired
    private UserLogic userLogic;

    @GetMapping("/verifyAccount")
    public ModelAndView verifyAccount(final @RequestParam(value = "uuid") String uuid) {
        final UserDto user = userLogic.byVerifyUUID(uuid);

        return createModelAndViewForAccountVerify(new PasswordFormModel(user.getUsername(), uuid));
    }

    @PostMapping("/verifyAccount")
    public ModelAndView verifyAccount(final @Valid @ModelAttribute("model") PasswordFormModel model,
                                      final BindingResult result) {
        if (result.hasFieldErrors()) {
            return createModelAndViewForAccountVerify(model);
        }

        if (!model.getPassword().equals(model.getConfirmPassword())) {
            result.addError(new FieldError("model", "password", null, false, new String[]{"unequal.passwords"}, null, "unknown"));
            return createModelAndViewForAccountVerify(model);
        }

        userLogic.verifyAccount(model.getUsername(), model.getUuid(), model.getPassword());

        return new ModelAndView("set_password_success", "message", "message.account.verified");
    }

    @GetMapping("/setPassword")
    public ModelAndView setPassword(final @RequestParam(value = "username") String username) {
        final UserDto user = userLogic.byUsername(username);
        return createModelAndViewForPasswordChange(new PasswordFormModel(user.getUsername()));
    }

    @PostMapping("/setPassword")
    public ModelAndView setPassword(final @Valid @ModelAttribute("model") PasswordFormModel model,
                                    final BindingResult result) {
        if (result.hasFieldErrors()) {
            return createModelAndViewForPasswordChange(model);
        }

        if (!model.getPassword().equals(model.getConfirmPassword())) {
            result.addError(new FieldError("model", "password", null, false, new String[]{"unequal.passwords"}, null, "unknown"));
            return createModelAndViewForPasswordChange(model);
        }

        userLogic.setPassword(model.getUsername(), model.getPassword());

        return new ModelAndView("set_password_success", "message", "message.password.changed");
    }

    private ModelAndView createModelAndViewForAccountVerify(final PasswordFormModel model) {
        return new ModelAndView("set_password", new HashMap<String, Object>() {{
            put("model", model);
            put("action", "/verifyAccount");
            put("title", "title.verify.account");
        }});
    }

    private ModelAndView createModelAndViewForPasswordChange(final PasswordFormModel model) {
        return new ModelAndView("set_password", new HashMap<String, Object>() {{
            put("model", model);
            put("action", "/setPassword");
            put("title", "title.change.password");
        }});
    }
}
