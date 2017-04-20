package at.rpisec.server.jpa.api;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

/**
 * This class is the root class for all entity types and provides proper implementations for hash and equals.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/14/17
 */
@MappedSuperclass
public abstract class BaseEntity<I extends Serializable> implements Serializable, Entity<I> {

    @Override
    public int hashCode() {
        return (getId() != null) ? getId().hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!this.getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        final BaseEntity other = (BaseEntity) obj;
        if ((other.getId() == null) && (getId() == null)) {
            return false;
        }

        return Objects.equals(getId(), other.getId());
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s]", this.getClass().getSimpleName(), (getId() == null) ? "null" : getId().toString());
    }
}
