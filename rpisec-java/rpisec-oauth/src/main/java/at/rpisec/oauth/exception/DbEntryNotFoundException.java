package at.rpisec.oauth.exception;

import at.rpisec.oauth.jpa.api.Entity;
import org.springframework.dao.DataAccessException;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
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
