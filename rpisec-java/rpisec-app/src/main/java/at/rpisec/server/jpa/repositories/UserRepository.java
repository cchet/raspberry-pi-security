package at.rpisec.server.jpa.repositories;

import at.rpisec.server.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.websocket.server.PathParam;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/15/17
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByUsernameAndVerifyDateNotNull(String username);

    @Query(name = "User.findByUsernameAndClientUuid")
    User findByUsernameAndClientUuid(@Param("username") String username,
                                     @Param("uuid") String uuid);

    User findByVerifyUUID(String uuid);
}
