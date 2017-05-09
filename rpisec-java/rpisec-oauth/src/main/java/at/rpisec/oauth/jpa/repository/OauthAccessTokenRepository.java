package at.rpisec.oauth.jpa.repository;

import at.rpisec.oauth.jpa.model.OauthAccessToken;
import at.rpisec.oauth.jpa.model.OauthClient;
import at.rpisec.oauth.jpa.model.OauthClientToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
public interface OauthAccessTokenRepository extends JpaRepository<OauthAccessToken, String> {

    @Query(name = "OauthAccessToken.findByClientTokenId")
    List<OauthAccessToken> findByClientTokenId(String tokenId);

    @Query(name = "OauthAccessToken.findByClientIdAndUsername")
    List<OauthAccessToken> findByClientIdAndUsername(String clientId, String username);

    @Query(name = "OauthAccessToken.findByRefreshTokenId")
    OauthAccessToken findByRefreshTokenId(String refreshTokenId);
}
