package at.rpisec.server.rest;

import at.rpisec.server.logic.api.UserLogic;
import at.rpisec.server.rest.model.UserDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/19/17
 */
@RestController
@RequestMapping("/api/user")
public class UserRestController extends AbstractRestController<UserDto, Long, UserLogic> {
}
