package ru.cip.ws.erp;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.common.CommonConstants;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.cip.ws.erp.jms.MQMessageListener;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static ru.cip.ws.erp.ConfigurationHolder.*;

/**
 * Author: Upatov Egor <br>
 * Date: 14.12.2016, 20:40 <br>
 * Description:  Главный конфиг
 */

@Configuration
@ComponentScan(basePackages = "ru.cip.ws.erp")
@EnableTransactionManagement
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


    @Bean(name = "dataSource")
    public DataSource lookupDatasource(@Qualifier("app.properties") Properties props) throws NamingException {
        final String jndiNameDatasource = props.getProperty(CFG_KEY_DATASOURCE_JNDI);
        log.info("Start lookup DataSource by jndiName='{}'", jndiNameDatasource);
        final Properties jndiProps = new Properties();
        jndiProps.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
        final JndiTemplate jndiFactory = new JndiTemplate(jndiProps);
        final DataSource result = jndiFactory.lookup(jndiNameDatasource, DataSource.class);
        log.info("DataSource from JNDI is [@{}]", Integer.toHexString(result.hashCode()));
        return result;
    }

    @Bean(name = "hibernateProperties")
    public Properties hibernateProperties() {
        final Properties result = new Properties();
        result.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
//        result.put("hibernate.show_sql", "false");
        result.put("hibernate.format_sql", "true");
        result.put("hibernate.max_fetch_depth", "3");
        result.put("hibernate.hbm2ddl.auto", ""); // means "NONE"
        return result;
    }


    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
            @Qualifier("dataSource") final DataSource dataSource,
            @Qualifier("hibernateProperties") final Properties props
    ) {
        final LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
        result.setDataSource(dataSource);
        result.setPackagesToScan("ru.cip.ws.erp.jpa.entity");
        result.setJpaProperties(props);
        result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        log.info("Initialized 'EntityManagerFactory'[@{}]: with DataSource[@{}]",
                Integer.toHexString(result.hashCode()),
                Integer.toHexString(dataSource.hashCode())
        );
        return result;
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") final EntityManagerFactory emf
    ) {
        final JpaTransactionManager result = new JpaTransactionManager(emf);
        result.setDefaultTimeout(36000);
        log.info("Initialized 'transactionManager'[@{}]: with EntityManagerFactory[@{}]",
                Integer.toHexString(result.hashCode()),
                Integer.toHexString(emf.hashCode())
        );
        return result;
    }


    @Bean(name = "mqConnectionFactory")
    public ConnectionFactory mqClientConnectionFactory(final ConfigurationHolder cfg) throws JMSException {
        final MQQueueConnectionFactory result = new MQQueueConnectionFactory();
        result.setConnectionNameList(cfg.get(CFG_KEY_MQ_HOSTS));
        result.setQueueManager(cfg.get(CFG_KEY_MQ_NAME));
        result.setCCSID(ObjectUtils.firstNonNull(cfg.getInteger(CFG_KEY_CCSID), 1208));
        result.setChannel(cfg.get(CFG_KEY_MQ_CHANNEL));
        result.setClientReconnectTimeout(600);
        result.setTransportType(CommonConstants.WMQ_CM_CLIENT);
        result.setClientReconnectOptions(CommonConstants.WMQ_CLIENT_RECONNECT);
        log.info("Initialized: {}", result);
        return result;
    }

    @Bean(name = "jmsConnectionFactorySecured")
    public ConnectionFactory jmsQueueConnectionFactorySecured(
            @Qualifier("mqConnectionFactory") ConnectionFactory factory,
            final ConfigurationHolder cfg
    ) {
        final UserCredentialsConnectionFactoryAdapter result = new UserCredentialsConnectionFactoryAdapter();
        result.setTargetConnectionFactory(factory);
        result.setUsername(cfg.get(CFG_KEY_MQ_LOGIN));
        result.setPassword(cfg.get(CFG_KEY_MQ_PASSWORD));
        log.info("Initialized 'jmsConnectionFactorySecured'[@{}]", Integer.toHexString(result.hashCode()));
        return result;
    }

    @Bean(name = "jmsTemplate")
    public JmsTemplate jmsTemplate(
            @Qualifier("jmsConnectionFactorySecured") ConnectionFactory connectionFactory
    ) {
        final JmsTemplate result = new JmsTemplate(connectionFactory);
        result.setReceiveTimeout(10000);
        result.setDestinationResolver(new DynamicDestinationResolver());
        log.info("Initialized 'JmsTemplate'[@{}]: with 'jmsConnectionFactorySecured'[@{}]",
                Integer.toHexString(result.hashCode()),
                Integer.toHexString(connectionFactory.hashCode())
        );
        return result;
    }

    @Bean(name = "consumerJmsListenerContainer")
    public DefaultMessageListenerContainer consumerJmsListenerContainer(
            @Qualifier("jmsConnectionFactorySecured") ConnectionFactory connectionFactory,
            final MQMessageListener listener,
            final ConfigurationHolder cfg
    ) {
        final DefaultMessageListenerContainer result = new DefaultMessageListenerContainer();
        result.setConnectionFactory(connectionFactory);
        result.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
        result.setMessageListener(listener);
        result.setDestinationName(cfg.get(CFG_KEY_QUEUE_RESPONSE));
        log.info("Initialized 'DefaultMessageListenerContainer'[@{}]: Listener[@{}], QUEUE='{}'",
                Integer.toHexString(result.hashCode()),
                Integer.toHexString(listener.hashCode()),
                cfg.get(CFG_KEY_QUEUE_RESPONSE)
        );
        return result;
    }


}
