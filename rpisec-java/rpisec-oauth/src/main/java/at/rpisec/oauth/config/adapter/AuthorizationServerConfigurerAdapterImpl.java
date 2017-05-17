package at.rpisec.oauth.config.adapter;

import at.rpisec.oauth.security.ClientUserAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
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
    private ClientUserAuthenticationManager authenticationManager;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    @Qualifier("customAuthorizationServerTokenServices")
    private AuthorizationServerTokenServices authorizationServerTokenServices;
    @Autowired
    private TokenGranter tokenGranter;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcClientDetailsService clientDetailsService;

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
                 .authenticationManager(authenticationManager)
                 .tokenServices(authorizationServerTokenServices)
                 .tokenGranter(tokenGranter);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }
}
