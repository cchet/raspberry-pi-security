package at.rpisec.app.jpa.api;

import java.io.Serializable;

/**
 * This interfaes marks an class as an entity.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/14/17
 */
public interface IEntity<T extends Serializable> {

    T getId();

    void setId(T id);
}
