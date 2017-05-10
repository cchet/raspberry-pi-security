package at.rpisec.server.config;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/23/17
 */
public class ServletContextConfig implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        FilterRegistration.Dynamic fr = servletContext.addFilter("resourceUrlEncodingFilter",
                                                                 new ResourceUrlEncodingFilter());
        fr.setInitParameter("encoding", "UTF-8");
        fr.setInitParameter("forceEncoding", "true");
        fr.addMappingForUrlPatterns(null, true, "/*");
    }
}
