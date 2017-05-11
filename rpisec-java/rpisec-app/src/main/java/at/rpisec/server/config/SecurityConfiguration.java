package at.rpisec.server.config;

import at.rpisec.server.config.adaptor.ResourceServerConfigurerAdapterImpl;
import at.rpisec.server.config.adaptor.WebSecurityConfigurerAdapterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Configuration
public class SecurityConfiguration {

    @Autowired
    private ConfigProperties.OauthProperties oauthProperties;

    @Bean
    PasswordEncoder producePasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ResourceServerConfigurerAdapter produceResourceServerConfigurerAdapter() {
        return new ResourceServerConfigurerAdapterImpl();
    }

    @Bean
    WebSecurityConfigurerAdapter produceWebSecurityConfigurerAdapter() {
        return new WebSecurityConfigurerAdapterImpl();
    }

    @Bean
    ResourceServerTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(oauthProperties.getCheckTokenEndpoint());
        tokenService.setClientId(oauthProperties.getClientId());
        tokenService.setClientSecret(oauthProperties.getClientSecret());
        tokenService.setTokenName("token");

        return tokenService;
    }
}
