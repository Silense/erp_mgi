package ru.cip.ws.erp.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.cip.ws.erp.business.IncomingMessageProcessor;
import ru.cip.ws.erp.factory.JAXBMarshallerUtil;
import ru.cip.ws.erp.generated.erptypes.*;
import ru.cip.ws.erp.jpa.dao.EnumDaoImpl;
import ru.cip.ws.erp.jpa.entity.RsysEnum;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Igor on 2014.07.31.
 * Modified by Egor on 2016.09.10.
 */

@Component
public class MQMessageListener implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(MQMessageListener.class);
    private static AtomicInteger counter = new AtomicInteger(0);


    @Autowired
    private IncomingMessageProcessor messageProcessor;

    @Autowired
    private EnumDaoImpl enumDao;

    @Override
    public void onMessage(Message message) {
        final int requestNumber = counter.incrementAndGet();
        if (!(message instanceof TextMessage)) {
            log.warn("#{} End. Is not TextMessage and will be skipped.", requestNumber);
            return;
        }
        try {
            // Process text message
            final TextMessage textMessage = (TextMessage) message;
            log.info("#{} MessageBody:\n{}\n", requestNumber, textMessage.getText());
            final ResponseMsg msg = JAXBMarshallerUtil.unmarshalResponse(textMessage.getText());
            if (msg == null) {
                log.error("#{} End. Cannot marshal to ResponseMsg", requestNumber);
                return;
            }
            final String uuid = msg.getRequestId();
            final StatusCode status = msg.getStatusCode();
            final String statusMessage = msg.getStatusMessage();
            final Date responseDate = toDate(msg.getResponseDate());
            processMessage(uuid, msg, status, statusMessage, responseDate, textMessage.getText());
        } catch (JMSException e) {
            log.error("#{} Exception in onMessage : ", requestNumber, e);
        } catch (JAXBException e) {
            log.error("#{} Cant parse to JAXB (XML type) ", requestNumber, e);
        }
    }

    /*
     * Converts java.util.Date to javax.xml.datatype.XMLGregorianCalendar
     */
    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date){
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(date);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
        } catch (DatatypeConfigurationException ex) {
            log.error("Cannot convert Date to XMLGregorianCalendar", ex);
        }
        return xmlCalendar;
    }

    /*
     * Converts XMLGregorianCalendar to java.util.Date in Java
     */
    public static Date toDate(XMLGregorianCalendar calendar){
        if(calendar == null) {
            return null;
        }
        return calendar.toGregorianCalendar().getTime();
    }

    private void processMessage(
            final String uuid,
            final ResponseMsg msg,
            final StatusCode status,
            final String statusMessage,
            final Date responseDate,
            final String rawContent
    ) {
        final RsysEnum erpStatus = enumDao.get("ERP_CONVERSATION_STATUS", status.name());
        final ResponseBody responseBody = msg.getResponseBody();
        if (responseBody == null) {
            messageProcessor.processStatusMessage(uuid, erpStatus, responseDate, statusMessage, rawContent);
            return;
        }
        final LetterFromERPType response = responseBody.getResponse();
        if(response == null){
            log.error("{} : К такому жизнь меня не готовила", uuid);
            return;
        }
        final MessageFromERPCommonType mc = response.getMessageCommon();
        if (mc != null) {
            if (mc.getFindInspectionResponse() != null) {
                messageProcessor.process(uuid, erpStatus, responseDate, rawContent, mc.getFindInspectionResponse());
            } else if (mc.getListOfProcsecutorsTerritorialJurisdictionResponse() != null) {
                messageProcessor.process(uuid, erpStatus, responseDate, rawContent, mc.getListOfProcsecutorsTerritorialJurisdictionResponse());
            } else if (mc.getERPResponse() != null) {
                messageProcessor.process(uuid, erpStatus, responseDate, rawContent, mc.getERPResponse());
            } else {
                log.warn("{} : Unknown messageType no processing", uuid);
            }
        }
        final MessageFromERP294Type m294 = response.getMessage294();
        if (m294 != null) {
            if (m294.getPlanRegular294Notification() != null) {
                messageProcessor.process(uuid, erpStatus, responseDate, rawContent, m294.getPlanRegular294Notification());
            } else if (m294.getPlanRegular294Response() != null) {
                messageProcessor.process(uuid, erpStatus, responseDate, rawContent, m294.getPlanRegular294Response());
            } else if (m294.getPlanResult294Notification() != null) {
                messageProcessor.process(uuid, erpStatus, responseDate, rawContent, m294.getPlanResult294Notification());
            } else if (m294.getPlanResult294Response() != null) {
                messageProcessor.process(uuid, erpStatus, responseDate, rawContent, m294.getPlanResult294Response());
            } else if (m294.getUplanResult294Notification() != null) {
                messageProcessor.process(uuid, erpStatus, responseDate, rawContent, m294.getUplanResult294Notification());
            } else if (m294.getUplanResult294Response() != null) {
                messageProcessor.process(uuid, erpStatus, responseDate, rawContent, m294.getUplanResult294Response());
            } else if (m294.getUplanUnregular294Notification() != null) {
                messageProcessor.process(uuid, erpStatus, responseDate, rawContent, m294.getUplanUnregular294Notification());
            } else if (m294.getUplanUnregular294Response() != null) {
                messageProcessor.process(uuid, erpStatus, responseDate, rawContent, m294.getUplanUnregular294Response());
            } else {
                log.warn("{} : Unknown messageType no processing", uuid);
            }
        }
        log.debug("{} : End processing", uuid);
    }


}
