package ru.cip.ws.erp.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Igor on 2014.07.31.
 * Modified by Egor on 2016.09.10.
 */
public class MQMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MQMessageSender.class);
    private static AtomicInteger counter  = new AtomicInteger(0);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void onMessage(Message message) {
        final int currentRequestNumber = counter.incrementAndGet();
        try {
            final String messageId = message.getJMSMessageID();
            logger.info("#{} Incoming message[{}] Type: {}", currentRequestNumber,  messageId,  message.getClass().getName());
            if (isTextMessage(message)) {
                // Process text message
                final TextMessage textMessage = (TextMessage) message;
                logger.debug("MessageBody: {}", textMessage.getText());
                //TODO
                // ready for reply
                final Destination replyTo = message.getJMSReplyTo();
                if (replyTo == null) { // quite eating messages without JMSReplyTo
                    logger.warn("#{} comes without JMSReplyTo", currentRequestNumber);
                } else {
                    reply(messageId, replyTo, currentRequestNumber);
                }
            } else {
                final String wrongTypeMessage = "We don't handle messages other then TextMessage.";
                logger.warn("#{} is not TextMessage", currentRequestNumber);
                throw new JMSException(wrongTypeMessage);
            }
        } catch (JMSException e) {
            logger.error("#{} Exception in onMessage : ", currentRequestNumber, e);
        }
    }

    private void reply(final String correlationId, Destination replyTo, final int currentRequestNumber) throws JMSException {
        logger.info("#{} Reply to {}", currentRequestNumber, replyTo);
        final AtomicReference<TextMessage> reposentRef = new AtomicReference<>();
        jmsTemplate.send(replyTo, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                final TextMessage response = session.createTextMessage();
                response.setJMSCorrelationID(correlationId);
                response.setText("Ok");
                response.setStringProperty("ToOrgCode", "200902");
                response.setStringProperty("FromOrgCode", "4028");
                reposentRef.set(response);
                return response;
            }
        });
        logger.info("#{} Sent reply for {} with MessageId={}", currentRequestNumber, correlationId, reposentRef.get().getJMSMessageID());
    }

    public boolean isTextMessage(Message message) {
        return message instanceof TextMessage;
    }
}
