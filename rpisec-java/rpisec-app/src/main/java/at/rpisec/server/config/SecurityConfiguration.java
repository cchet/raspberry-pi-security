package at.rpisec.server.config;

import at.rpisec.server.config.adaptor.ResourceServerConfigurerAdapterImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    PasswordEncoder producePasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ResourceServerConfigurerAdapter produceResourceServerConfigurerAdapter() {
        return new ResourceServerConfigurerAdapterImpl();
    }

    @Bean
    ResourceServerTokenServices tokenService(final ConfigProperties.RpisecProperties rpisecProperties) {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(rpisecProperties.getCheckTokenEndpoint());
        tokenService.setClientId(rpisecProperties.getClientId());
        tokenService.setClientSecret(rpisecProperties.getClientSecret());
        tokenService.setTokenName("token");

        return tokenService;
    }
}
