package at.rpisec.oauth.logic.impl;

import at.rpisec.oauth.jpa.model.OauthAccessToken;
import at.rpisec.oauth.jpa.model.OauthClient;
import at.rpisec.oauth.jpa.model.OauthRefreshToken;
import at.rpisec.oauth.jpa.model.OauthUser;
import at.rpisec.oauth.jpa.repository.OauthAccessTokenRepository;
import at.rpisec.oauth.jpa.repository.OauthClientRepository;
import at.rpisec.oauth.jpa.repository.OauthRefreshTokenRepository;
import at.rpisec.oauth.jpa.repository.OauthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Component
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
public class JpaTokenStoreImpl implements TokenStore {

    @Autowired
    private OauthAccessTokenRepository accessTokenRepo;
    @Autowired
    private OauthRefreshTokenRepository refreshTokenRepo;
    @Autowired
    private OauthUserRepository userRepo;
    @Autowired
    private OauthClientRepository clientRepo;

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(extractTokenKey(token.getValue()));
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        final OauthAccessToken accessToken = accessTokenRepo.findOne(extractTokenKey(token));
        OAuth2Authentication authentication = null;

        if (accessToken != null) {
            try {
                authentication = SerializationUtils.deserialize(accessToken.getAuthentication());
            } catch (IllegalArgumentException e) {
                accessTokenRepo.delete(accessToken);
            }
        }

        return authentication;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token,
                                 OAuth2Authentication authentication) {
        OauthRefreshToken refreshToken = null;
        if (token.getRefreshToken() != null) {
            refreshToken = refreshTokenRepo.findOne(extractTokenKey(token.getRefreshToken().getValue()));
        }

        final String accessTokenId = extractTokenKey(token.getValue());
        final OauthAccessToken oldAccessToken = accessTokenRepo.findOne(accessTokenId);
        if (oldAccessToken != null) {
            accessTokenRepo.delete(oldAccessToken);
        }

        final OauthClient client = clientRepo.findByClientId(authentication.getOAuth2Request().getClientId());
        OauthUser user = null;
        if (!authentication.isClientOnly()) {
            user = userRepo.findByUsername(authentication.getName());
        }

        final OauthAccessToken accessToken = new OauthAccessToken(accessTokenId,
                                                                  SerializationUtils.serialize(token),
                                                                  authenticationKeyGenerator.extractKey(authentication),
                                                                  SerializationUtils.serialize(authentication),
                                                                  user,
                                                                  client);
        accessToken.setRefreshToken(refreshToken);

        accessTokenRepo.save(accessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken oauthAccessToken = null;

        final OauthAccessToken accessToken = accessTokenRepo.findOne(extractTokenKey(tokenValue));
        if (accessToken != null) {
            try {
                oauthAccessToken = SerializationUtils.deserialize(accessToken.getToken());
            } catch (Exception e) {
                removeAccessToken(tokenValue);
            }
        }

        return oauthAccessToken;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        removeAccessToken(token.getValue());
    }

    private void removeAccessToken(String tokenValue) {
        final OauthAccessToken accessToken = accessTokenRepo.findOne(extractTokenKey(tokenValue));
        if (accessToken != null) {
            accessTokenRepo.delete(accessToken);
        }
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken oauthAccessToken = null;

        final String tokenId = authenticationKeyGenerator.extractKey(authentication);
        final OauthAccessToken accessToken = accessTokenRepo.findOne(tokenId);
        if (accessToken != null) {
            try {
                oauthAccessToken = SerializationUtils.deserialize(accessToken.getToken());
                if (!tokenId.equals(authenticationKeyGenerator.extractKey(readAuthentication(oauthAccessToken.getValue())))) {
                    removeAccessToken(oauthAccessToken.getValue());
                    storeAccessToken(oauthAccessToken, authentication);
                }
            } catch (IllegalArgumentException e) {

            }
        }

        return oauthAccessToken;
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken _refreshToken,
                                  OAuth2Authentication _authentication) {
        final String accessTokenId = extractTokenKey(_refreshToken.getValue());
        final OauthAccessToken accessToken = accessTokenRepo.findOne(accessTokenId);

        if (accessToken != null) {
            OauthRefreshToken refreshToken = new OauthRefreshToken(extractTokenKey(_refreshToken.getValue()),
                                                                   SerializationUtils.serialize(_refreshToken),
                                                                   SerializationUtils.serialize(authenticationKeyGenerator));
            refreshToken = refreshTokenRepo.save(refreshToken);
            accessToken.setRefreshToken(refreshToken);
            accessTokenRepo.save(accessToken);
        }
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        OAuth2RefreshToken oAuth2RefreshToken = null;
        final OauthRefreshToken refreshToken = refreshTokenRepo.findOne(extractTokenKey(tokenValue));
        if (refreshToken != null) {
            try {
                oAuth2RefreshToken = SerializationUtils.deserialize(refreshToken.getToken());
            } catch (IllegalArgumentException e) {
                removeRefreshToken(tokenValue);
            }
        }

        return oAuth2RefreshToken;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        removeRefreshToken(token.getValue());
    }

    private void removeRefreshToken(String token) {
        refreshTokenRepo.delete(token);
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        OAuth2Authentication oauthAuthentication = null;
        final OauthRefreshToken refreshToken = refreshTokenRepo.findOne(extractTokenKey(token.getValue()));
        if (refreshToken != null) {
            try {
                oauthAuthentication = SerializationUtils.deserialize(refreshToken.getAuthentication());
            } catch (IllegalArgumentException e) {
                removeRefreshToken(token.getValue());
            }
        }

        return oauthAuthentication;
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken _refreshToken) {
        final OauthAccessToken accessToken = accessTokenRepo.findByRefreshTokenId(extractTokenKey(_refreshToken.getValue()));
        if (accessToken != null) {
            accessTokenRepo.delete(accessToken);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId,
                                                                         String userName) {
        return accessTokenRepo.findByClientIdAndUsername(clientId, userName).stream()
                              .map(accessToken -> (OAuth2AccessToken) SerializationUtils.deserialize(accessToken.getToken()))
                              .collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        return accessTokenRepo.findByClientTokenId(clientId).stream()
                              .map(accessToken -> (OAuth2AccessToken) SerializationUtils.deserialize(accessToken.getToken()))
                              .collect(Collectors.toList());
    }

    private String extractTokenKey(String value) {
        if (value == null) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        try {
            byte[] bytes = digest.digest(value.getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }
}
