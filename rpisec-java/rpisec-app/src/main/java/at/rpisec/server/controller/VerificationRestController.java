package at.rpisec.server.controller;

import at.rpisec.server.logic.api.UserLogic;
import at.rpisec.server.shared.rest.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.websocket.server.PathParam;

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
        return new ModelAndView("verify_account", "user", user);
    }

    @GetMapping("/verifySetPassword")
    public String verifyAccount(final @RequestParam(value = "uuid") String uuid,
                                final @RequestParam(value = "password") String password) {
        userLogic.verifyAccount(uuid, password);
        return "verified_account";
    }
}
