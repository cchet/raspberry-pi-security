package at.rpisec.app.config.adaptor;

import at.rpisec.app.config.other.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.ContentVersionStrategy;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

/**
 * This class configures the web mvc resources.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
public class WebMvcConfigurerAdapterImpl extends WebMvcConfigurerAdapter {

    @Autowired
    private ConfigProperties.WebjarProperties webjarProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/bootstrap/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/bootstrap/" + webjarProperties.getBootstrap() + "/")
                .setCacheControl(CacheControl.empty().cachePublic())
                .resourceChain(false)
                .addResolver(createResourceResolver());

        registry.addResourceHandler("/jquery/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/jquery/" + webjarProperties.getJquery() + "/")
                .setCacheControl(CacheControl.empty().cachePublic())
                .resourceChain(false)
                .addResolver(createResourceResolver());
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
        return new ResourceUrlEncodingFilter();
    }

    private ResourceResolver createResourceResolver() {
        final VersionResourceResolver resolver = new VersionResourceResolver();
        resolver.addVersionStrategy(new ContentVersionStrategy(), "/**");

        return resolver;
    }
}
