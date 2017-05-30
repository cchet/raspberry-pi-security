package at.rpisec.oauth.exception;

import at.rpisec.oauth.jpa.api.Entity;
import org.springframework.dao.DataAccessException;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
public class DbEntryNotFoundException extends DataAccessException {

    private final Class<?> entityClass;

    public DbEntryNotFoundException(String message,
                                    Class<?> entityClass) {
        super(message);
        this.entityClass = entityClass;
    }

    public DbEntryNotFoundException(String message,
                                    Throwable cause,
                                    Class<? extends Entity> entityClass) {
        super(message, cause);
        this.entityClass = entityClass;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }
}
