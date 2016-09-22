package ru.cip.ws.erp.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import ru.cip.ws.erp.factory.JAXBMarshallerUtil;
import ru.cip.ws.erp.generated.erptypes.*;
import ru.cip.ws.erp.jdbc.dao.ImportSessionDaoImpl;
import ru.cip.ws.erp.jdbc.entity.ImpSession;
import ru.cip.ws.erp.jdbc.entity.ImpSessionEvent;

import javax.jms.*;
import javax.xml.bind.JAXBException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Igor on 2014.07.31.
 * Modified by Egor on 2016.09.10.
 */
public class MQMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MQMessageSender.class);
    private static AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ImportSessionDaoImpl importSessionDao;

    @Override
    public void onMessage(Message message) {
        final int requestNumber = counter.incrementAndGet();
        try {
            final String messageId = message.getJMSMessageID();
            logger.info("#{} Incoming message[{}] Type: {}", requestNumber, messageId, message.getClass().getName());
            if (isTextMessage(message)) {
                // Process text message
                final TextMessage textMessage = (TextMessage) message;
                logger.debug("MessageBody:\n{}\n", textMessage.getText().substring(0, Math.min(1200, textMessage.getText().length())));
                final ResponseMsg msg = JAXBMarshallerUtil.unmarshalResponse(textMessage.getText());
                final ImpSession importSession = importSessionDao.createNewImportSession(
                        "Ответ от ЕРП", msg.getStatusMessage(), msg.getRequestId()
                );
                logger.info("#{} Create new ImportSession: {}", requestNumber, importSession);
                final String messageType = getMessageType(msg);
                logger.info("#{} Message of type \'{}\'", requestNumber, messageType);
                final ImpSessionEvent importEvent = importSessionDao.createNewImportEvent(messageType, importSession);
                logger.info("#{} Create new ImportEvent: {}", requestNumber, importEvent);
                processMessage(msg, requestNumber);
                // ready for reply
                final Destination replyTo = message.getJMSReplyTo();
                if (replyTo == null) { // quite eating messages without JMSReplyTo
                    logger.warn("#{} comes without JMSReplyTo", requestNumber);
                } else {
                    reply(messageId, replyTo, requestNumber);
                }
            } else {
                final String wrongTypeMessage = "We don't handle messages other then TextMessage.";
                logger.warn("#{} is not TextMessage", requestNumber);
                throw new JMSException(wrongTypeMessage);
            }
        } catch (JMSException e) {
            logger.error("#{} Exception in onMessage : ", requestNumber, e);
        } catch (JAXBException e) {
            logger.error("#{} Cant parse to JAXB (XML type) ", e);
        }
    }

    private void processMessage(final ResponseMsg msg, final int requestNumber) {

    }

    private String getMessageType(final ResponseMsg msg) {
        final ResponseBody responseBody = msg.getResponseBody();
        if (responseBody != null) {
            final LetterFromERPType response = responseBody.getResponse();
            if (response != null) {
                final MessageFromERPCommonType messageCommon = response.getMessageCommon();
                final StringBuilder sb = new StringBuilder();
                if (messageCommon != null) {
                    sb.append("MessageCommon.");
                    if (messageCommon.getFindInspectionResponse() != null) {
                        return sb.append("FindInspectionResponse").toString();
                    } else if (messageCommon.getListOfProcsecutorsTerritorialJurisdictionResponse() != null) {
                        return sb.append("ListOfProcsecutorsTerritorialJurisdictionResponse").toString();
                    } else if (messageCommon.getERPResponse() != null) {
                        return sb.append("ErpResponse<>").toString();
                    } else {
                        return sb.append("#!Unknown").toString();
                    }
                }
                final MessageFromERP294Type message294 = response.getMessage294();
                if (message294 != null) {
                    sb.append("Message294.");
                    if (message294.getPlanRegular294Notification() != null) {
                        return sb.append("PlanRegular294Notification").toString();
                    } else if (message294.getPlanRegular294Response() != null) {
                        return sb.append("PlanRegular294Response").toString();
                    } else if (message294.getPlanResult294Notification() != null) {
                        return sb.append("PlanResult294Notification").toString();
                    } else if (message294.getPlanResult294Response() != null) {
                        return sb.append("PlanResult294Response").toString();
                    } else if (message294.getUplanResult294Notification() != null) {
                        return sb.append("UplanResult294Notification").toString();
                    } else if (message294.getUplanResult294Response() != null) {
                        return sb.append("UplanResult294Response").toString();
                    } else if (message294.getUplanUnregular294Notification() != null) {
                        return sb.append("UplanUnregular294Notification").toString();
                    } else if (message294.getUplanUnregular294Response() != null) {
                        return sb.append("UplanUnregular294Response").toString();
                    } else {
                        return sb.append("#!Unknown").toString();
                    }
                }
            }
        }
        return "StatusMessage";
    }

    private void reply(final String correlationId, Destination replyTo, final int currentRequestNumber) throws JMSException {
        logger.info("#{} Reply to {}", currentRequestNumber, replyTo);
        final AtomicReference<TextMessage> reposentRef = new AtomicReference<>();
        jmsTemplate.send(
                replyTo, new MessageCreator() {
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
                }
        );
        logger.info("#{} Sent reply for {} with MessageId={}", currentRequestNumber, correlationId, reposentRef.get().getJMSMessageID());
    }

    public boolean isTextMessage(Message message) {
        return message instanceof TextMessage;
    }
}
