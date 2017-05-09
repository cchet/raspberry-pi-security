package at.rpisec.oauth.jpa.repository;

import at.rpisec.oauth.jpa.model.OauthClientToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Repository
public interface OauthClientTokenRepository extends JpaRepository<OauthClientToken, Long> {

    @Query(name = "OauthClientToken.findByClientTokenId")
    OauthClientToken findByClientTokenId(String tokenId);

}
