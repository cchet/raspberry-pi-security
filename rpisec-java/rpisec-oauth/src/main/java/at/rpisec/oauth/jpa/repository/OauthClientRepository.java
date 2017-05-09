package at.rpisec.oauth.jpa.repository;

import at.rpisec.oauth.jpa.model.OauthClient;
import at.rpisec.oauth.jpa.model.OauthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Repository
public interface OauthClientRepository extends JpaRepository<OauthClient, Long> {

    OauthClient findByClientId(String clientId);
}
