package at.rpisec.server.jpa.repositories;

import at.rpisec.server.jpa.model.Client;
import at.rpisec.server.jpa.model.User;
import at.rpisec.server.jpa.projection.ClientFirebaseToken;
import at.rpisec.server.jpa.projection.EntityLongId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByUuidAndUser(String uuid,
                             User user);

    List<ClientFirebaseToken> findDistinctByFirebaseTokenIsNotNull();
}
