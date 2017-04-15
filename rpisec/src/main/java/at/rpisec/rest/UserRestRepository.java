package at.rpisec.rest;

import at.rpisec.application.SecurityProperties;
import at.rpisec.jpa.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/15/17
 */
@PreAuthorize("hasRole('" + SecurityProperties.ROLE_ADMIN + "')")
@RepositoryRestResource(path = "/users", itemResourceRel = "user", collectionResourceRel = "list", exported = true)
public interface UserRestRepository extends CrudRepository<User, Long> {

    @Override
    User save(User entity);

    @Override
    void delete(Long aLong);
}
