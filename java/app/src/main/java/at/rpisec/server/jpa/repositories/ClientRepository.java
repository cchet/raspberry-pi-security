package at.rpisec.server.jpa.repositories;

import at.rpisec.server.jpa.model.Client;
import at.rpisec.server.jpa.model.ClientId;
import at.rpisec.server.jpa.projection.IClientFirebaseToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/20/17
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, ClientId> {

    List<IClientFirebaseToken> findDistinctByFcmTokenIsNotNull();
}
