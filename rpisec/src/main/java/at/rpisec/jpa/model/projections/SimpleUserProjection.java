package at.rpisec.jpa.model.projections;

import at.rpisec.jpa.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/15/17
 */
@Projection(name = "simpleUser", types = User.class)
public interface SimpleUserProjection {

    @Value("#{target.lastname}, #{target.firstname}")
    String getFullName();

    String getEmail();

    Boolean getAdmin();
}
