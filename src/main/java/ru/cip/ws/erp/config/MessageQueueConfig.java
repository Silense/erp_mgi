package ru.cip.ws.erp.config;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.common.CommonConstants;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import ru.cip.ws.erp.ConfigurationHolder;
import ru.cip.ws.erp.jms.MQMessageListener;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

import static ru.cip.ws.erp.ConfigurationHolder.*;

/**
 * Author: Upatov Egor <br>
 * Date: 11.01.2017, 23:40 <br>
 * Description:
 */

@Configuration
public class MessageQueueConfig {

    private static final Logger log = LoggerFactory.getLogger("CONFIG");

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
