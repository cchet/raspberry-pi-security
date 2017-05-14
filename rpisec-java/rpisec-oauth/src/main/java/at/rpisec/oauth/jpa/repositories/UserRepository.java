package at.rpisec.oauth.jpa.repositories;

import at.rpisec.oauth.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/15/17
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByUsernameAndVerifiedAtNotNull(String username);

    User findByVerifyUUID(String uuid);
}
