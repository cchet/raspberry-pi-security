package at.rpisec.oauth.logic.impl;

import at.rpisec.oauth.jpa.model.OauthClient;
import at.rpisec.oauth.jpa.model.OauthClientToken;
import at.rpisec.oauth.jpa.repository.OauthClientRepository;
import at.rpisec.oauth.jpa.repository.OauthClientTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA implementation for OAUTH client service.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Component
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
public class ClientTokenServiceImpl implements ClientTokenServices {

    @Autowired
    private OauthClientTokenRepository clientTokenRepo;
    @Autowired
    private OauthClientRepository clientRepo;

    private ClientKeyGenerator keyGenerator;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OAuth2AccessToken getAccessToken(OAuth2ProtectedResourceDetails resource,
                                            Authentication authentication) {

        OAuth2AccessToken accessToken = null;
        final OauthClientToken clientToken = clientTokenRepo.findByClientTokenId(resource.getClientId());

        if (clientToken != null) {
            accessToken = SerializationUtils.deserialize(clientToken.getToken());
        }

        return accessToken;
    }

    @Override
    public void saveAccessToken(OAuth2ProtectedResourceDetails resource,
                                Authentication authentication,
                                OAuth2AccessToken accessToken) {
        final OauthClient client = clientRepo.findByClientId(resource.getClientId());
        if (client != null) {
            final OauthClientToken clientToken = new OauthClientToken(client.getId(),
                                                                      SerializationUtils.serialize(accessToken),
                                                                      client);
            clientTokenRepo.saveAndFlush(clientToken);
        }
    }

    @Override
    public void removeAccessToken(OAuth2ProtectedResourceDetails resource,
                                  Authentication authentication) {
        final OauthClientToken clientToken = clientTokenRepo.findByClientTokenId(resource.getClientId());
        if (clientToken != null) {
            clientTokenRepo.delete(clientToken);
        }
    }
}
