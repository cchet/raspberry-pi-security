package at.rpisec.oauth.jpa.repository;

import at.rpisec.oauth.jpa.model.OauthAccessToken;
import at.rpisec.oauth.jpa.model.OauthRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Repository
public interface OauthRefreshTokenRepository extends JpaRepository<OauthRefreshToken, String> {
}
