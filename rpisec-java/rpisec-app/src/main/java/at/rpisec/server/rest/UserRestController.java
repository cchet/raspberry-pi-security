package at.rpisec.server.rest;

import at.rpisec.server.logic.api.UserLogic;
import at.rpisec.server.shared.rest.constants.UserRestConstants;
import at.rpisec.server.shared.rest.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/19/17
 */
@RestController
@RequestMapping(UserRestConstants.REST_BASE)
public class UserRestController {

    @Autowired
    private UserLogic logic;

    @GetMapping(value = UserRestConstants.REL_URI_LIST)
    public List<UserDto> list() {
        return logic.list();
    }

    @GetMapping(value = UserRestConstants.REL_URI_GET + "/{id}")
    public UserDto get(@PathVariable Long id) {
        return logic.byId(id);
    }

    @DeleteMapping(value = UserRestConstants.REL_URI_DELETE + "/{id}")
    public void delete(@PathVariable Long id) {
        logic.delete(id);
    }

    @PutMapping(value = UserRestConstants.REL_URI_CREATE)
    public UserDto create(@RequestBody @Valid UserDto model) {
        return logic.byId(logic.create(model));
    }

    @PutMapping(value = UserRestConstants.REL_URI_UPDATE)
    public UserDto update(@RequestBody @Valid UserDto model) {
        logic.update(model);
        return logic.byId(model.getId());
    }
}
