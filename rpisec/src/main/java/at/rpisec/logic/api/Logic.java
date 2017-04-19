package at.rpisec.logic.api;

import java.io.Serializable;
import java.util.List;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/19/17
 */
public interface Logic<T, I extends Serializable> {

    T byId(I id);

    List<T> list();

    T create(T model);

    T update(T model);

    boolean delete(I id);
}
