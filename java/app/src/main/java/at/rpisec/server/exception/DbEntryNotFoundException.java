package at.rpisec.server.exception;

import at.rpisec.server.jpa.api.IEntity;
import org.springframework.dao.DataAccessException;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
public class DbEntryNotFoundException extends DataAccessException {

    private final Class<? extends IEntity> entityClass;

    public DbEntryNotFoundException(String message,
                                    Class<? extends IEntity> entityClass) {
        super(message);
        this.entityClass = entityClass;
    }

    public DbEntryNotFoundException(String message,
                                    Throwable cause,
                                    Class<? extends IEntity> entityClass) {
        super(message, cause);
        this.entityClass = entityClass;
    }

    public Class<? extends IEntity> getEntityClass() {
        return entityClass;
    }
}
