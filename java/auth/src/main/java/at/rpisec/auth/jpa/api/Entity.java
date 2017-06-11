package at.rpisec.auth.jpa.api;

import java.io.Serializable;

/**
 * This interfaes marks an class as an entity.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/14/17
 */
public interface Entity<T extends Serializable> {

    T getId();

    void setId(T id);
}
