package at.rpisec.app.jpa.projection;

import at.rpisec.app.jpa.model.Client;
import org.springframework.beans.factory.annotation.Value;

/**
 * Projection to {@link Client#firebaseToken} selection only.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/07/17
 */
public interface IEntityLongId {

    @Value("#{target.id}")
    Long getId();
}
