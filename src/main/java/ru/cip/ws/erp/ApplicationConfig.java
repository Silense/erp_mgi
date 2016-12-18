package ru.cip.ws.erp;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.common.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.cip.ws.erp.jms.MQMessageListener;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

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
    private static final String JNDI_NAME_DATASOURCE = "DATASOURCE";

    @Bean(name="wmq.properties")
    public Properties wmqProperties(){
        return loadPropertiesFromFile("wmq.properties");
    }

    @Bean(name="db.properties")
    public Properties dbProperties(){
        return loadPropertiesFromFile("db.properties");
    }

    @Bean(name="app.properties")
    public Properties appProperties(){
        return loadPropertiesFromFile("app.properties");
    }


    private Properties loadPropertiesFromFile(final String path) {
        final Properties result = new Properties();
        try {
            result.load(getClass().getClassLoader().getResourceAsStream(path));
        } catch (IOException e) {
           log.error("Cannot load properties file [{}]:",path,  e);
        }
        return result;
    }

    @Bean(name = "mqConnectionFactory")
    public ConnectionFactory mqClientConnectionFactory( @Qualifier("wmq.properties") Properties props) throws JMSException {
        final MQQueueConnectionFactory result = new MQQueueConnectionFactory();
        result.setConnectionNameList(props.getProperty("wmq.qmgr.hosts"));
        result.setQueueManager(props.getProperty("wmq.qmgr.name"));
        result.setCCSID(Integer.parseInt(props.getProperty("wmq.qmgr.ccid")));
        result.setChannel(props.getProperty("wmq.qmgr.channel"));
        result.setClientReconnectTimeout(Integer.parseInt(props.getProperty("wmq.qmgr.clientReconnectTimeout")));
        result.setTransportType(CommonConstants.WMQ_CM_CLIENT);
        result.setClientReconnectOptions(CommonConstants.WMQ_CLIENT_RECONNECT);
        log.info("Initialized: {}", result);
        return result;
    }

    @Bean(name= "jmsConnectionFactorySecured")
    public ConnectionFactory jmsQueueConnectionFactorySecured(
            @Qualifier("mqConnectionFactory") ConnectionFactory factory,
            @Qualifier("wmq.properties") Properties props
    ){
        final UserCredentialsConnectionFactoryAdapter result = new UserCredentialsConnectionFactoryAdapter();
        result.setTargetConnectionFactory(factory);
        result.setUsername(props.getProperty("wmq.qmgr.login"));
        result.setPassword(props.getProperty("wmq.qmgr.password"));
        log.info("Initialized: {}", result);
        return result;
    }

    @Bean(name="jmsTemplate")
    public JmsTemplate jmsTemplate(
            @Qualifier("jmsConnectionFactorySecured") ConnectionFactory connectionFactory
    ){
        final JmsTemplate result = new JmsTemplate(connectionFactory);
        result.setReceiveTimeout(10000);
        result.setDestinationResolver(new DynamicDestinationResolver());
        return result;
    }

    @Bean(name = "consumerJmsListenerContainer")
    public DefaultMessageListenerContainer consumerJmsListenerContainer(
            @Qualifier("jmsConnectionFactorySecured") ConnectionFactory connectionFactory,
            final MQMessageListener listener,
            @Qualifier("wmq.properties") Properties props
    ) {
        final DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer();
        messageListenerContainer.setConnectionFactory(connectionFactory);
        messageListenerContainer.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
        messageListenerContainer.setMessageListener(listener);
        messageListenerContainer.setDestinationName(props.getProperty("wmq.queue.response"));
        return messageListenerContainer;
    }

    @Bean(name = "dataSource")
    public DataSource lookupDatasource(@Qualifier("db.properties") Properties props) {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(props.getProperty("jdbc.driver"));
        dataSource.setUrl(props.getProperty("jdbc.url"));
        dataSource.setUsername(props.getProperty("jdbc.username"));
        dataSource.setPassword(props.getProperty("jdbc.password"));
        return dataSource;

    }

    @Bean(name ="hibernateProperties")
    public Properties hibernateProperties() {
        final Properties result = new Properties();
        result.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
//        result.put("hibernate.show_sql", "false");
        result.put("hibernate.format_sql", "true");
        result.put("hibernate.max_fetch_depth", "3");
        result.put("hibernate.hbm2ddl.auto", ""); // means "NONE"
        return result;
    }


    @Bean(name="sessionFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
            @Qualifier("dataSource") final DataSource dataSource,
            @Qualifier("hibernateProperties") final Properties props
    ) {
        final LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
        result.setDataSource(dataSource);
        result.setPackagesToScan("ru.cip.ws.erp.jpa.entity");
        result.setJpaProperties(props);
        result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        log.info("Initialized 'hospitalSessionFactory'[@{}]", Integer.toHexString(result.hashCode()));
        return result;
    }

    @Bean(name ="transactionManager")
    public JpaTransactionManager transactionManager(
            @Qualifier("sessionFactory") final EntityManagerFactory emf,
            @Qualifier("dataSource") final DataSource dataSource
    ) {
        final JpaTransactionManager result = new JpaTransactionManager(emf);
        result.setDefaultTimeout(36000);
        log.info("Initialized 'transactionManager'[@{}]", Integer.toHexString(result.hashCode()));
        return result;
    }


}
