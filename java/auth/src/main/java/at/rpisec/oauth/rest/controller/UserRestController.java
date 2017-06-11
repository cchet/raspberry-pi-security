package at.rpisec.oauth.rest.controller;

import at.rpisec.oauth.logic.api.UserLogic;
import at.rpisec.server.shared.rest.constants.AuthRestConstants;
import at.rpisec.server.shared.rest.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
@RestController
@RequestMapping(AuthRestConstants.USER_REST_PAI_BASE)
public class UserRestController {

    @Autowired
    private UserLogic logic;

    @GetMapping(value = AuthRestConstants.REL_URI_LIST)
    public List<UserDto> list() {
        return logic.list();
    }

    @GetMapping(value = AuthRestConstants.REL_URI_GET + "/{username}")
    public UserDto get(@PathVariable String username) {
        return logic.byUsername(username);
    }

    @DeleteMapping(value = AuthRestConstants.REL_URI_DELETE + "/{username}")
    public void delete(@PathVariable String username) {
        logic.delete(username);
    }

    @PostMapping(value = AuthRestConstants.REL_URI_CREATE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public UserDto create(@RequestBody @Valid UserDto model) {
        final Long id = logic.create(model);
        return logic.byId(id);
    }

    @PostMapping(value = AuthRestConstants.REL_URI_UPDATE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public UserDto update(@RequestBody @Valid UserDto model) {
        final Long id = logic.update(model);
        return logic.byId(id);
    }
}
