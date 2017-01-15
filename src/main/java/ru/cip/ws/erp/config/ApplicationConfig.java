package ru.cip.ws.erp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Author: Upatov Egor <br>
 * Date: 14.12.2016, 20:40 <br>
 * Description:  Главный конфиг
 */

@Configuration
@ComponentScan(basePackages = "ru.cip.ws.erp")
@EnableTransactionManagement
@Import({PersistenceConfig.class, MessageQueueConfig.class, SchedulerConfig.class})
public class ApplicationConfig {
    private static final Logger log = LoggerFactory.getLogger("CONFIG");

    @Bean(name = "app.properties")
    public Properties appProperties() {
        final String path = "app.properties";
        final Properties result = new Properties();
        try {
            result.load(getClass().getClassLoader().getResourceAsStream(path));
        } catch (IOException e) {
            log.error("Cannot load properties file [{}]:", path, e);
        }
        log.info("Read config file [{}]", path);
        if (log.isDebugEnabled()) {
            for (Map.Entry<Object, Object> entry : result.entrySet()) {
                log.debug("{} = \'{}\'", entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    @Bean
    public AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter(){
        AnnotationMethodHandlerAdapter result = new AnnotationMethodHandlerAdapter();
        final HttpMessageConverter<?>[] messageConverters = result.getMessageConverters();
        for (int i = 0; i < messageConverters.length; i++) {
            HttpMessageConverter<?> messageConverter = messageConverters[i];
            if(messageConverter instanceof StringHttpMessageConverter){
                final StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
                final List<MediaType> supportedMediaTypes = new ArrayList<>(stringHttpMessageConverter.getSupportedMediaTypes());
                supportedMediaTypes.add(MediaType.TEXT_HTML);
                supportedMediaTypes.add(MediaType.TEXT_PLAIN);
                supportedMediaTypes.add(new MediaType("text","plain", StandardCharsets.UTF_8));
                supportedMediaTypes.add(new MediaType("text","html", StandardCharsets.UTF_8));
                supportedMediaTypes.add(new MediaType("*","*", StandardCharsets.UTF_8));
                stringHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
                messageConverters[i] = stringHttpMessageConverter;
                break;
            }
        }
        result.setMessageConverters(messageConverters);
        return result;
    }

}
