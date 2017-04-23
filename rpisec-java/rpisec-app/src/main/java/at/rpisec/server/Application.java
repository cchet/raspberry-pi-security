package at.rpisec.server;

import at.rpisec.server.config.*;
import at.rpisec.server.jpa.model.User;
import at.rpisec.server.jpa.repositories.UserRepository;
import at.rpisec.server.security.DbUsernamePasswordAuthenticationManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Locale;

@SpringBootApplication
@ComponentScan(basePackageClasses = {Application.class, Jsr310JpaConverters.class})
@EnableWebMvc
@EnableJpaRepositories(basePackageClasses = {UserRepository.class})
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableSwagger2
@EntityScan(basePackageClasses = User.class)
@Import({
        BeanValidatorPluginsConfiguration.class
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    WebSecurityConfigurerAdapter produceWebSecurityConfigurerAdapter() {
        return new SecurityConfiguration();
    }

    @Bean
    WebMvcConfigurerAdapter produceWebMvcConfigurationAdaptor() {
        return new WebMvConfiguration();
    }

    @Bean
    ServletContextInitializer produceWebApplicationInitializer(){
        return new ServletContextConfig();
    }

    @Bean
    Docket produceSwaggerDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/rpisec")
                .select()
                .paths(PathSelectors.ant("/rest/**"))
                .build();
    }

    @Bean
    AuthenticationManager produceAuthManager() {
        return new DbUsernamePasswordAuthenticationManager();
    }

    @Bean
    PasswordEncoder producePasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Scope("prototype")
    Logger logger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
    }

    @Bean
    public MapperFacade produceConfigurableMapper() {
        return new ModelMapperConfigurer();
    }

    // See: https://firebase.google.com/docs/auth/admin/create-custom-tokens

    @Bean
    FirebaseApp produceFirebaseApp(final ConfigProperties.FirebaseProperties firebaseConfig) throws IOException {
        final File file = Paths.get(firebaseConfig.getConfigFile()).toFile();
        if (!file.exists()) {
            throw new IllegalArgumentException("firebaseConfig: '" + firebaseConfig.getConfigFile() + "' does not exist");
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(FileUtils.openInputStream(file))
                .setDatabaseUrl(firebaseConfig.getDatabaseUrl())
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    DatabaseReference produceDatabaseReference(FirebaseApp firebaseApp) {
        FirebaseDatabase.getInstance(firebaseApp).setPersistenceEnabled(false);
        return FirebaseDatabase.getInstance(firebaseApp).getReference();
    }

    @Bean
    FirebaseAuth produceFirebaseAuth(final FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }

    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages/message", "classpath:messages/email", "classpath:messages/validation");
        messageSource.setDefaultEncoding("UTF8");
        messageSource.setCacheSeconds(60 * 60 * 24);
        return messageSource;
    }

    @Bean("localeResolver")
    LocaleResolver produceLocaleResolver() {
        return new FixedLocaleResolver(Locale.US);
    }

    /**
     * Parameter must be provided if in dev mode otherwise injection will fail because values are unknown at the poit
     * the command line runner gets executed.
     *
     * @param port        the server.port property value
     * @param address     the server.address property value
     * @param sslEnabled  the server.ssl.enabled property value
     * @param contextPath the server.context-path property value
     * @return the build server root url
     */
    @Bean("serverUrl")
    String produceRootUrl(@Value("${server.port}") Integer port,
                          @Value("${server.address}") String address,
                          @Value("${server.ssl.enabled}") Boolean sslEnabled,
                          @Value("${server.context-path}") String contextPath) {
        return (sslEnabled ? "https://" : "http://") + address + ":" + port + contextPath;
    }
}
