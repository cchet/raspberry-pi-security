package at.rpisec.auth.config.other;

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
     * The necessary vm options
     */
    public static final class VMOptions {

        public static final String ADMIN_EMAIL = "admin.email";

        private VMOptions() {
        }
    }

    /**
     * The supported profiles
     */
    public static final class SupportedProfiles {
        public static final String PROD = "prod";
        public static final String DEV = "dev";
        public static final String TEST = "test";
        public static final String INTEGRATION_TEST = "integrationTest";
    }

    /**
     * The firebase configuration properties
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
     * The webjar configuration properties
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
     * The rpisec configuration properties
     */
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
