package at.rpisec.server.controller;

import at.rpisec.server.logic.api.UserLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
@Controller
public class VerificationRestController {

    @Autowired
    private UserLogic userLogic;

    @GetMapping("/verifyEmail")
    public String verifyEmail(final @RequestParam(value = "uuid", required = false) String uuid) {
        return "verify_email";
    }
}
