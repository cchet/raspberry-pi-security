package at.rpisec.app.config.other;

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

    /**
     * Specifies the supported profiles
     */
    public static final class SupportedProfiles {
        public static final String PROD = "prod";
        public static final String DEV = "dev";
        public static final String TEST = "test";
        public static final String INTEGRATION_TEST = "integrationTest";
    }

    /**
     * Specifies the firebase properties.
     */
    @Component
    @ConfigurationProperties("firebase")
    @Getter
    @Setter
    @NoArgsConstructor
    public static final class FirebaseProperties {

        private String databaseUrl;
        private String configFile;
        private String cloudMsgApiKey;
        private String cloudMessagingUrl;
    }

    /**
     * Specifies the webjars properties.
     */
    @Component
    @ConfigurationProperties("webjar")
    @Getter
    @Setter
    @NoArgsConstructor
    public static final class WebjarProperties {

        private String jquery;
        private String bootstrap;

    }

    /**
     * Specifies the rpisec oauth properties.
     */
    @Component
    @ConfigurationProperties("rpisec-oauth")
    @Getter
    @Setter
    @NoArgsConstructor
    public static final class OauthProperties {

        private String baseUrl;
        private String resourceId;
        private String clientId;
        private String clientSecret;
        private String checkTokenEndpoint;
        private String getTokenEndpoint;
        private String systemUser;
        private String systemPassword;
    }
}
