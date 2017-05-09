package at.rpisec.oauth.logic.impl;

import at.rpisec.oauth.jpa.model.OauthCode;
import at.rpisec.oauth.jpa.repository.OauthCodeRepository;
import org.omg.CORBA.CODESET_INCOMPATIBLE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Component
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
public class JpaAuthorizationCodeServiceImpl extends RandomValueAuthorizationCodeServices {

    @Autowired
    private OauthCodeRepository coderepo;

    @Override
    protected void store(String _code,
                         OAuth2Authentication _authentication) {
        final OauthCode code = new OauthCode(_code, SerializationUtils.serialize(_authentication));
        coderepo.save(code);
    }

    @Override
    protected OAuth2Authentication remove(String _code) {
        OAuth2Authentication authentication = null;
        final OauthCode code = coderepo.findOne(_code);
        if(code != null) {
            try {
                authentication = SerializationUtils.deserialize(code.getAuthentication());
            } catch (IllegalArgumentException e) {

            }
        }

        if(authentication != null) {
            code.setAuthentication(SerializationUtils.serialize(authentication));
            coderepo.save(code);
        }

        return authentication;
    }
}
