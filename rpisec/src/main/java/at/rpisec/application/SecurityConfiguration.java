package at.rpisec.application;

import at.rpisec.security.AdminRestAccessDecisionVoter;
import at.rpisec.security.ClientRestAccessDecisionVoter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * This configuration class setups the security for this application.
 * <p>
 * See: http://www.baeldung.com/spring-security-custom-voter
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/14/17
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public AccessDecisionManager produceAccessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = Arrays.asList(
                new AdminRestAccessDecisionVoter(),
                new ClientRestAccessDecisionVoter());

        return new UnanimousBased(decisionVoters);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/rest/admin/**").hasRole("ADMIN").anyRequest().authenticated()
            .antMatchers("/rest/client/**").hasRole("CLIENT").anyRequest().authenticated()
            .accessDecisionManager(produceAccessDecisionManager());
    }
}
