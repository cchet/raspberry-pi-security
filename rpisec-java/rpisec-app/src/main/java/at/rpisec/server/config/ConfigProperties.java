package at.rpisec.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
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
    @ConfigurationProperties("firebase")
    public static final class FirebaseProperties {

        @Getter
        @Setter
        private String databaseUrl;

        @Getter
        @Setter
        private String configFile;

        @Getter
        @Setter
        private String cloudMsgApiKey;

        @Getter
        @Setter
        private String cloudMessagingUrl;
    }

    @Component
    @ConfigurationProperties("webjar")
    public static final class WebjarProperties {

        @Getter
        @Setter
        private String jquery;

        @Getter
        @Setter
        private String bootstrap;

    }
}
