package at.rpisec.server.config.adaptor;

import at.rpisec.server.config.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.ContentVersionStrategy;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
public class WebMvcConfigurerAdapterImpl extends WebMvcConfigurerAdapter {

    @Autowired
    private ConfigProperties.WebjarProperties webjarProperties;

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(new ThymeleafViewResolver());
    }

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

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/css/")
                .setCacheControl(CacheControl.empty().cachePublic())
                .resourceChain(false)
                .addResolver(createResourceResolver());

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/js/")
                .setCacheControl(CacheControl.empty().cachePublic())
                .resourceChain(false)
                .addResolver(createResourceResolver());
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
