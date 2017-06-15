package at.rpisec.auth.config;

import at.rpisec.auth.config.adapter.AuthorizationServerConfigurerAdapterImpl;
import at.rpisec.auth.config.adapter.WebSecurityConfigurerAdapterImpl;
import at.rpisec.auth.config.other.ConfigProperties;
import at.rpisec.auth.security.ClientUserAuthenticationManager;
import at.rpisec.auth.security.ClientUserDetailsService;
import at.rpisec.auth.security.ClientUserPasswordTokenGranter;
import at.rpisec.shared.rest.constants.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * This class holds all producers for security relevant beans such as oauth2 provided beans and app server constants.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Configuration
public class SecurityConfiguration {

    public static final String QUALIFIER_APP_SERVER_RESOURCE_ID = "QUALIFIER_APP_SERVER_RESOURCE_ID";

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ConfigProperties.RpisecProperties rpisecProperties;

    @Bean
    WebSecurityConfigurerAdapter produceWebSecurityConfigurerAdapter() {
        return new WebSecurityConfigurerAdapterImpl();
    }

    @Bean
    AuthorizationServerConfigurerAdapter produceAuthorizationServerConfigurerAdapter() {
        return new AuthorizationServerConfigurerAdapterImpl();
    }

    @Bean
    PasswordEncoder createPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService produceUserDetailsManager() {
        return new ClientUserDetailsService();
    }

    @Bean
    AuthenticationManager produceAuthenticationManager() {
        return new ClientUserAuthenticationManager();
    }

    @Bean
    AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    @Primary
    public JdbcClientDetailsService produceJdbcClientDetailsService(final PasswordEncoder passwordEncoder) {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder);

        return clientDetailsService;
    }

    @Bean
    TokenStore createTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    AuthenticationKeyGenerator produceAuthenticationKeyGenerator() {
        return (auth) -> UUID.randomUUID().toString();
    }

    @Bean
    @Qualifier("customAuthorizationServerTokenServices")
    AuthorizationServerTokenServices produceAuthorizationServerTokenServices(final ClientDetailsService clientDetailsService,
                                                                             final AuthenticationManager authenticationManager,
                                                                             final TokenStore tokenStore) {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setAuthenticationManager(authenticationManager);
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);
        defaultTokenServices.setAccessTokenValiditySeconds(SecurityConstants.TOKEN_VALIDITY_DURATION_SECONDS);
        defaultTokenServices.setRefreshTokenValiditySeconds(SecurityConstants.REFRESH_TOKEN_VALIDITY_DURATION_SECONDS);

        return defaultTokenServices;
    }

    @Bean
    OAuth2RequestFactory produceOAuth2RequestFactory(final ClientDetailsService clientDetailsService) {
        return new DefaultOAuth2RequestFactory(clientDetailsService);
    }

    @Bean
    TokenGranter produceTokenGranter(final AuthenticationManager authenticationManager,
                                     @Qualifier("customAuthorizationServerTokenServices") final AuthorizationServerTokenServices authorizationServerTokenServices,
                                     final ClientDetailsService clientDetailsService,
                                     final OAuth2RequestFactory oAuth2RequestFactory) {
        return new ClientUserPasswordTokenGranter(authenticationManager,
                                                  authorizationServerTokenServices,
                                                  clientDetailsService,
                                                  oAuth2RequestFactory);
    }

    @Bean
    @Scope("prototype")
    @Qualifier(QUALIFIER_APP_SERVER_RESOURCE_ID)
    String produceAppServerResourceId() {
        return rpisecProperties.getResourceId();
    }
}
