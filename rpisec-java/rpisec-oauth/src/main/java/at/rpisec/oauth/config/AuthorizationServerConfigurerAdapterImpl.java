package at.rpisec.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/29/17
 */
public class AuthorizationServerConfigurerAdapterImpl extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private PasswordEncoder pwdEncoder;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(pwdEncoder);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(createTokenStore())
                 .authenticationManager(new OAuth2AuthenticationManager());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource)
               .passwordEncoder(pwdEncoder)
               .withClient("my-trusted-client")
               .secret("123456789")
               .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
               .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
               .scopes("read", "write")
               .resourceIds("oauth2-resource")
               .accessTokenValiditySeconds(60)
               .and()
               .withClient("my-client-with-registered-redirect")
               .secret("123456789")
               .authorizedGrantTypes("authorization_code")
               .authorities("ROLE_CLIENT").scopes("read", "trust")
               .resourceIds("oauth2-resource")
               .redirectUris("http://anywhere?key=value")
               .and()
               .withClient("my-client-with-secret")
               .secret("123456789")
               .authorizedGrantTypes("client_credentials", "password")
               .authorities("ROLE_CLIENT").scopes("read")
               .resourceIds("oauth2-resource").secret("secret");
    }

    @Bean
    AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    TokenStore createTokenStore() {
        return new JdbcTokenStore(dataSource);
    }
}
