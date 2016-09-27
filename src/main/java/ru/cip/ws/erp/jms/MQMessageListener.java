package ru.cip.ws.erp.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import ru.cip.ws.erp.business.IncomingMessageProcessor;
import ru.cip.ws.erp.factory.JAXBMarshallerUtil;
import ru.cip.ws.erp.generated.erptypes.*;
import ru.cip.ws.erp.jdbc.dao.ExportSessionDaoImpl;
import ru.cip.ws.erp.jdbc.dao.ImportSessionDaoImpl;
import ru.cip.ws.erp.jdbc.entity.ExpSession;
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

    private static final Logger logger = LoggerFactory.getLogger(MQMessageListener.class);
    private static AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ImportSessionDaoImpl importSessionDao;
    @Autowired
    private ExportSessionDaoImpl exportSessionDao;
    @Autowired
    private IncomingMessageProcessor messageProcessor;

    @Override
    public void onMessage(Message message) {
        final int requestNumber = counter.incrementAndGet();
        try {
            final String messageId = message.getJMSMessageID();
            logger.info("#{} Incoming message[{}] Type: {}", requestNumber, messageId, message.getClass().getName());
            if (isTextMessage(message)) {
                // Process text message
                final TextMessage textMessage = (TextMessage) message;
                logger.info(
                        "#{} MessageBody:\n{}\n", requestNumber, textMessage.getText().substring(
                                0, Math.min(
                                        1200, textMessage.getText().length()
                                )
                        )
                );
                final ResponseMsg msg = JAXBMarshallerUtil.unmarshalResponse(textMessage.getText());
                if (msg == null) {
                    logger.error("#{} End. Cannot marshal to ResponseMsg", requestNumber);
                    return;
                }
                final String requestId = msg.getRequestId();
                logger.info("#{} RequestId = \'{}\'", requestNumber, requestId);
                ImpSession importSession = importSessionDao.getByEXT_PACKAGE_ID(requestId);
                if (importSession != null) {
                    logger.info("{} : Found existed ImportSession[EXT_PACKAGE_ID]: {}", requestId, importSession);
                } else {
                    importSession = importSessionDao.createNewImportSession("Ответ от ЕРП", msg.getStatusMessage(), requestId);
                    logger.info("{} : Created ImportSession: {}", requestId, importSession);
                }
                final String messageType = getMessageType(msg);
                logger.info("{} : Message of type \'{}\'", requestId, messageType);
                final ImpSessionEvent importEvent = importSessionDao.createNewImportEvent(messageType, importSession);
                logger.info("{} : Created ImportEvent: {}", requestId, importEvent);
                processMessage(requestId, msg, importSession);
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
            logger.error("#{} Cant parse to JAXB (XML type) ", requestNumber, e);
        }
    }

    private void processMessage(final String requestId, final ResponseMsg msg, final ImpSession importSession) {
        final String statusMessage = msg.getStatusMessage();
        logger.debug("{} : Start processing with statusMessage=\'{}\'", requestId, statusMessage);
        if (importSession.getEXP_SESSION_ID() != null) {
            final ExpSession expSession = exportSessionDao.getSessionById(importSession.getEXP_SESSION_ID());
            if (expSession != null) {
                expSession.setIMP_SESSION_ID(importSession.getIMP_SESSION_ID());
                expSession.setSESSION_MSG(msg.getStatusCode().toString());
                exportSessionDao.merge(expSession);
            }
        }
        final ResponseBody responseBody = msg.getResponseBody();
        if (responseBody != null) {
            final LetterFromERPType response = responseBody.getResponse();
            if (response != null) {
                final MessageFromERPCommonType messageCommon = response.getMessageCommon();
                if (messageCommon != null) {
                    if (messageCommon.getFindInspectionResponse() != null) {
                        messageProcessor.process(requestId, messageCommon.getFindInspectionResponse(), statusMessage);
                    } else if (messageCommon.getListOfProcsecutorsTerritorialJurisdictionResponse() != null) {
                        messageProcessor.process(
                                requestId, messageCommon.getListOfProcsecutorsTerritorialJurisdictionResponse(), statusMessage
                        );
                    } else if (messageCommon.getERPResponse() != null) {
                        messageProcessor.process(requestId, messageCommon.getERPResponse(), statusMessage);
                    } else {
                        logger.warn("{} : Unknown messageType no processing", requestId);
                    }
                }
                final MessageFromERP294Type message294 = response.getMessage294();
                if (message294 != null) {
                    if (message294.getPlanRegular294Notification() != null) {
                        messageProcessor.process(requestId, message294.getPlanRegular294Notification(), statusMessage);
                    } else if (message294.getPlanRegular294Response() != null) {
                        messageProcessor.process(requestId, message294.getPlanRegular294Response(), statusMessage);
                    } else if (message294.getPlanResult294Notification() != null) {
                        messageProcessor.process(message294.getPlanResult294Notification());
                    } else if (message294.getPlanResult294Response() != null) {
                        messageProcessor.process(message294.getPlanResult294Response());
                    } else if (message294.getUplanResult294Notification() != null) {
                        messageProcessor.process(message294.getUplanResult294Notification());
                    } else if (message294.getUplanResult294Response() != null) {
                        messageProcessor.process(message294.getUplanResult294Response());
                    } else if (message294.getUplanUnregular294Notification() != null) {
                        messageProcessor.process(message294.getUplanUnregular294Notification());
                    } else if (message294.getUplanUnregular294Response() != null) {
                        messageProcessor.process(message294.getUplanUnregular294Response());
                    } else {
                        logger.warn("{} : Unknown messageType no processing", requestId);
                    }
                }
            }
        }
        logger.debug("{} : End processing", requestId);
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
