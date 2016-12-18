package ru.cip.ws.erp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.lang.management.ManagementFactory;

/**
 * Author: Upatov Egor <br>
 * Date: 14.12.2016, 20:43 <br>
 * Description:
 */
public class WebAppInitializer implements WebApplicationInitializer {
    private static final Logger log = LoggerFactory.getLogger("ROOT");
    private static AnnotationConfigWebApplicationContext context;

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        final String pid = ManagementFactory.getRuntimeMXBean().getName();
        log.info("PID {}: Start application", pid.split("@")[0]);
        context = new AnnotationConfigWebApplicationContext();
        context.setId("ERP_WS");
        context.register(ApplicationConfig.class);
        context.setServletContext(servletContext);
        final ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
        log.info("[{}] End initialization of application. Good luck!", context.getId());
    }
}
