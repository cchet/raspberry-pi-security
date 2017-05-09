package at.rpisec.oauth.jpa.repository;

import at.rpisec.oauth.jpa.model.OauthAccessToken;
import at.rpisec.oauth.jpa.model.OauthApprovals;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
public interface OauthApprovalsRepository extends JpaRepository<OauthApprovals, Long> {


}
