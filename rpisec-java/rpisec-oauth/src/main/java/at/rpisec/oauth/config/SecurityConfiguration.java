package at.rpisec.oauth.config;

import at.rpisec.oauth.config.adapter.AuthorizationServerConfigurerAdapterImpl;
import at.rpisec.oauth.config.adapter.WebSecurityConfigurerAdapterImpl;
import at.rpisec.oauth.config.other.ConfigProperties;
import at.rpisec.oauth.logic.impl.DomainAuthenticationManager;
import at.rpisec.oauth.logic.impl.DomainUserDetailsService;
import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Configuration
public class SecurityConfiguration {

    public static final String QUALIFIER_APP_SERVER_RESOURCE_ID = "QUALIFIER_APP_SERVER_RESOURCE_ID";
    public static final String QUALIFIER_OAUTH_URL_REGISTER_CLIENT = "QUALIFIER_OAUTH_URL_CREATE_CLIENT";
    public static final String QUALIFIER_OAUTH_URL_UNREGISTER_CLIENT = "QUALIFIER_OAUTH_URL_UNREGISTER_CLIENT";
    public static final String QUALIFIER_OAUTH_REST_TEMPLATE = "QUALIFIER_OAUTH_REST_TEMPLATE";

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
        return new DomainUserDetailsService();
    }

    @Bean
    AuthenticationManager produceAuthenticationManager() {
        return new DomainAuthenticationManager();
    }

    @Bean
    AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    TokenStore createTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    JdbcClientDetailsService produceClientRegistrationService() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Bean
    AuthenticationKeyGenerator produceAuthenticationKeyGenerator() {
        return (auth) -> UUID.randomUUID().toString();
    }

    @Bean
    @Scope("prototype")
    @Qualifier(QUALIFIER_APP_SERVER_RESOURCE_ID)
    String produceAppServerResourceId() {
        return rpisecProperties.getResourceId();
    }


    @Bean
    @Scope("prototype")
    @Qualifier(QUALIFIER_OAUTH_URL_REGISTER_CLIENT)
    String produceOAuthRegisterClientUrl() {
        return rpisecProperties.getBaseUrl() + ClientRestConstants.URI_REGISTER;
    }

    @Bean
    @Scope("prototype")
    @Qualifier(QUALIFIER_OAUTH_URL_UNREGISTER_CLIENT)
    String produceOAuthUnregsiterClientUrl() {
        return rpisecProperties.getBaseUrl() + ClientRestConstants.URI_UNREGISTER;
    }

    @Bean
    @Scope("prototype")
    @Qualifier(QUALIFIER_OAUTH_REST_TEMPLATE)
    RestTemplate produceFcmRestTemplate(final Logger log) {
        final RestTemplate oauthRestTemplate = new RestTemplate();
        final String auth = rpisecProperties.getSystemUser() + ":" + rpisecProperties.getSystemPassword();
        final String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(Charset.forName("US-ASCII")));

        oauthRestTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        oauthRestTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().put(HttpHeaders.AUTHORIZATION, Collections.singletonList(String.format("Basic %s", encodedAuth)));
            return execution.execute(request, body);
        });

        oauthRestTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return !HttpStatus.OK.equals(response.getStatusCode());
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                log.error("Could not post oauth client to app server");
                throw new IllegalStateException(String.format("Could not invoke rest api on authorization server. HttpStatus: %s",
                                                              response.getStatusCode()));
            }
        });

        return oauthRestTemplate;
    }
}
