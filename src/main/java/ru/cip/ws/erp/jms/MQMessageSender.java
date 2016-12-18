package ru.cip.ws.erp.jms;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import ru.cip.ws.erp.factory.JAXBMarshallerUtil;
import ru.cip.ws.erp.generated.erptypes.RequestMsg;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBElement;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Created by Igor on 2014.07.31.
 * Modified by Egor on 2016.09.10.
 */

@Component
public class MQMessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MQMessageSender.class);

    private final JmsTemplate jmsTemplate;
    private final String destinationQueue;

    @Autowired
    public MQMessageSender(@Qualifier("wmq.properties") final Properties props, final JmsTemplate jmsTemplate) {
        this.destinationQueue = props.getProperty("wmq.queue.request");
        this.jmsTemplate = jmsTemplate;
    }

    public String send(final long requestNumber, JAXBElement<RequestMsg> requestMessage) throws JMSException {
        final String logMark  = "#"+requestNumber;
        final String messageAsString = JAXBMarshallerUtil.marshalAsString(logMark, requestMessage);
        logger.debug("{} : MESSAGE BODY:\n {}", logMark, messageAsString);
        if (StringUtils.isEmpty(messageAsString)) {
            return null;
        }
       return send(messageAsString);

    }

    public final String send(final String message) throws JMSException {
        final AtomicReference<TextMessage> messageAtomicReference = new AtomicReference<>();
        jmsTemplate.send(destinationQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage();
                tm.setText(message);
                messageAtomicReference.set(tm);
                return tm;
            }
        });
        return messageAtomicReference.get().getJMSMessageID();
    }
}
