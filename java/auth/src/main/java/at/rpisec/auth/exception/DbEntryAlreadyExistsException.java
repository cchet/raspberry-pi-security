package at.rpisec.auth.exception;

import at.rpisec.auth.jpa.api.Entity;
import org.springframework.dao.DataAccessException;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
public class DbEntryAlreadyExistsException extends DataAccessException {

    private final Class<? extends Entity> entityClass;

    public DbEntryAlreadyExistsException(String message,
                                         Class<? extends Entity> entityClass) {
        super(message);
        this.entityClass = entityClass;
    }

    public DbEntryAlreadyExistsException(String message,
                                         Throwable cause,
                                         Class<? extends Entity> entityClass) {
        super(message, cause);
        this.entityClass = entityClass;
    }

    public Class<? extends Entity> getEntityClass() {
        return entityClass;
    }
}
