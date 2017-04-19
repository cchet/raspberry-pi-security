package at.rpisec;

import at.rpisec.config.ConfigProperties;
import at.rpisec.config.ModelMapperConfigurer;
import at.rpisec.config.SecurityConfiguration;
import at.rpisec.jpa.model.User;
import at.rpisec.jpa.repositories.UserRepository;
import at.rpisec.security.DbUsernamePasswordAuthenticationManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@SpringBootApplication
@ComponentScan(basePackageClasses = Application.class)
@EnableJpaRepositories(basePackageClasses = {UserRepository.class})
@EntityScan(basePackageClasses = User.class)
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableSwagger2
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
    public ConfigurableMapper produceConfigurableMapper() {
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
}
