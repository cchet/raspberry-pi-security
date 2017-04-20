package at.rpisec.server.exception;

import at.rpisec.server.jpa.api.Entity;
import org.springframework.dao.DataAccessException;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/20/17
 */
public class DbEntryNotFoundException extends DataAccessException {

    private final Class<? extends Entity> entityClass;

    public DbEntryNotFoundException(String message,
                                    Class<? extends Entity> entityClass) {
        super(message);
        this.entityClass = entityClass;
    }

    public DbEntryNotFoundException(String message,
                                    Throwable cause,
                                    Class<? extends Entity> entityClass) {
        super(message, cause);
        this.entityClass = entityClass;
    }

    public Class<? extends Entity> getEntityClass() {
        return entityClass;
    }
}
