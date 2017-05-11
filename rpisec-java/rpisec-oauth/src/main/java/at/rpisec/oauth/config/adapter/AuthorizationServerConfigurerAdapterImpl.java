package at.rpisec.oauth.config.adapter;

import at.rpisec.oauth.config.other.ConfigProperties;
import at.rpisec.oauth.logic.impl.DomainAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;

/**
 * url: https://spring.io/guides/tutorials/spring-boot-oauth2/
 * token
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/29/17
 */
public class AuthorizationServerConfigurerAdapterImpl extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder pwdEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private DomainAuthenticationManager authenticationManager;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ConfigProperties.RpisecProperties rpisecProperties;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients()
                .passwordEncoder(pwdEncoder);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                 .userDetailsService(userDetailsService)
                 .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource)
               .passwordEncoder(pwdEncoder)
               .withClient(rpisecProperties.getClientId())
               .resourceIds(rpisecProperties.getResourceId())
               .secret(rpisecProperties.getClientSecret())
               .scopes("trust")
               .authorizedGrantTypes("client_credentials")
               .autoApprove(true);
    }
}
