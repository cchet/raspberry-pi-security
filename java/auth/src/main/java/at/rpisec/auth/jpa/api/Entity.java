package at.rpisec.auth.jpa.api;

import java.io.Serializable;

/**
 * This interface marks an class as an entity.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/14/17
 */
public interface Entity<T extends Serializable> {

    /**
     * @return the entity id
     */
    T getId();

    /**
     * @param id the entity id to be set
     */
    void setId(T id);
}
