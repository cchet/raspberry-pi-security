package at.rpisec.oauth.config.other;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This class specifies all type safe configuration classes.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/17/17
 */
public class ConfigProperties {

    private ConfigProperties() {
    }

    @Component
    @ConfigurationProperties("webjar")
    @Getter
    @Setter
    @NoArgsConstructor
    public static final class WebjarProperties {

        private String jquery;
        private String bootstrap;

    }

    @Component
    @ConfigurationProperties("rpisec")
    @Getter
    @Setter
    @NoArgsConstructor
    public static final class RpisecProperties {

        private String baseUrl;
        private String resourceId;
        private String clientId;
        private String clientSecret;
        private String systemUser;
        private String systemPassword;
    }
}
