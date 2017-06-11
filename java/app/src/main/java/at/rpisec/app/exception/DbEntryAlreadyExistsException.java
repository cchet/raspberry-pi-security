package at.rpisec.app.exception;

import at.rpisec.app.jpa.api.IEntity;
import org.springframework.dao.DataAccessException;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
public class DbEntryAlreadyExistsException extends DataAccessException {

    private final Class<? extends IEntity> entityClass;

    public DbEntryAlreadyExistsException(String message,
                                         Class<? extends IEntity> entityClass) {
        super(message);
        this.entityClass = entityClass;
    }

    public DbEntryAlreadyExistsException(String message,
                                         Throwable cause,
                                         Class<? extends IEntity> entityClass) {
        super(message, cause);
        this.entityClass = entityClass;
    }

    public Class<? extends IEntity> getEntityClass() {
        return entityClass;
    }
}
