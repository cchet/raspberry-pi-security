package at.rpisec.rest;

import at.rpisec.logic.api.Logic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * This is the base class which manages the base crud operations for hosted entities.
 * A concrete class may enhance the available ret operations or denies some of them by overwriting them and throwing an exception.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/19/17
 */
public abstract class AbstractRestController<M, I extends Serializable, T extends Logic<M, I>> {

    @Autowired
    private T logic;

    @GetMapping(value = "/list")
    public List<M> getByAll() {
        return logic.list();
    }

    @GetMapping(value = "/{id}")
    public M getById(@PathVariable I id) {
        return logic.byId(id);
    }

    @DeleteMapping(value = "/{id}")
    public boolean delete(@PathVariable I id) {
        return logic.delete(id);
    }

    @PutMapping(value = "/update")
    public M update(@RequestBody @Valid M model) {
        return logic.update(model);
    }
}
