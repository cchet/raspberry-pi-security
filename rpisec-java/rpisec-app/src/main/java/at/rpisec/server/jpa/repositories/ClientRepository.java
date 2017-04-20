package at.rpisec.server.jpa.repositories;

import at.rpisec.server.jpa.model.Client;
import at.rpisec.server.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/20/17
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByUuidAndUser(String uuid,
                             User user);
}
