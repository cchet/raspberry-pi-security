package at.rpisec.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This class specifies all type safe configuration classes.
 *
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/17/17
 */
public class ConfigProperties {

    private ConfigProperties() {
    }

    @Component
    @ConfigurationProperties("firebase")
    public static final class FirebaseProperties {

        private String databaseUrl;
        private String configFile;

        public String getDatabaseUrl() {
            return databaseUrl;
        }

        public void setDatabaseUrl(String databaseUrl) {
            this.databaseUrl = databaseUrl;
        }

        public String getConfigFile() {
            return configFile;
        }

        public void setConfigFile(String configFile) {
            this.configFile = configFile;
        }
    }
}
