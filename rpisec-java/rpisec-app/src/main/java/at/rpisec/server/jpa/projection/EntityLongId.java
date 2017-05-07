package at.rpisec.server.jpa.projection;

import org.springframework.beans.factory.annotation.Value;

/**
 * Projection to {@link at.rpisec.server.jpa.model.Client#firebaseToken} selection only.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/07/17
 */
public interface EntityLongId {

    @Value("#{target.id}")
    Long getId();
}
