package ru.cip.ws.erp.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Created by Igor on 2014.07.31.
 * Modified by Egor on 2016.09.10.
 */
public class MQMessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MQMessageSender.class);
    private static AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    private JmsTemplate jmsTemplate;

    private String destinationQueue;

    private String replyToQueue;

    public String getDestinationQueue() {
        return destinationQueue;
    }


    public void setDestinationQueue(String destinationQueue) {
        this.destinationQueue = destinationQueue;
    }

    public String getReplyToQueue() {
        return replyToQueue;
    }

    public void setReplyToQueue(String replyToQueue) {
        this.replyToQueue = replyToQueue;
    }

    /**
     * Use injected destinationQueue as target.
     *
     * @param message
     */
    public String send(final String message) {
        final int requestNumber = counter.incrementAndGet();
        logger.info("#{} Start send message to \'{}\'", requestNumber, getDestinationQueue());
        try {
            final String result = send(message, getDestinationQueue(), requestNumber);
            logger.info("#{} Sent. Result is \'{}\'", requestNumber, result);
            return result;
        } catch (final JMSException e) {
            logger.error("#{} Error in JMS session  : ", requestNumber, e);
            return null;
        }
    }

    public final String send(final String message, final String destinationQueue, final int requestNumber) throws JMSException {
        final AtomicReference<TextMessage> messageAtomicReference = new AtomicReference<>();
        jmsTemplate.send(
                destinationQueue, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        TextMessage tm = session.createTextMessage();
                        tm.setText(message);
                        if (needReply()) {
                            tm.setJMSReplyTo(jmsTemplate.getDestinationResolver().resolveDestinationName(session, getReplyToQueue(), false));
                        }
                        messageAtomicReference.set(tm);
                        logger.debug("#{} Message is \n{}", requestNumber, tm.getText());
                        return tm;
                    }
                }
        );
        return messageAtomicReference.get().getJMSMessageID();
    }

    private boolean needReply() {
        return getReplyToQueue() != null;
    }
}
