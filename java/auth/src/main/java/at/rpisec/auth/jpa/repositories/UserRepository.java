package at.rpisec.auth.jpa.repositories;

import at.rpisec.auth.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The repository for the {@link User} entity type.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/15/17
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByUsernameAndVerifiedAtNotNull(String username);

    @Query("SELECT DISTINCT user FROM User user INNER JOIN user.clientDevices device WHERE user.username = :username AND device.clientId = :clientId")
    User findByUsernameAndClientDeviceId(@Param("username") String username,
                                         @Param("clientId") String clientId);

    User findByVerifyUUID(String uuid);
}
