package at.rpisec.rest;

import at.rpisec.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/15/17
 */
//@Secured(SecurityProperties.ROLE_ADMIN)
@RepositoryRestResource(path = "user")
public interface UserRestRepository extends JpaRepository<User, Long> {

    @RestResource(path = "byUsername")
    List<User> findByUsername(String username);
}
