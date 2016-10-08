package ru.cip.ws.erp.business;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.factory.JAXBMarshallerUtil;
import ru.cip.ws.erp.factory.MessageFactory;
import ru.cip.ws.erp.generated.erptypes.MessageToERP294Type;
import ru.cip.ws.erp.generated.erptypes.ProsecutorAskType;
import ru.cip.ws.erp.generated.erptypes.RequestMsg;
import ru.cip.ws.erp.jdbc.dao.ExportSessionDaoImpl;
import ru.cip.ws.erp.jdbc.dao.PlanCheckErpDaoImpl;
import ru.cip.ws.erp.jdbc.dao.PlanCheckRecordErpDaoImpl;
import ru.cip.ws.erp.jdbc.entity.*;
import ru.cip.ws.erp.jms.MQMessageSender;

import javax.xml.bind.JAXBElement;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: Upatov Egor <br>
 * Date: 18.09.2016, 14:24 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
@Transactional
public class MessageService {

    private final static Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageFactory messageFactory;
    @Autowired
    private MQMessageSender messageSender;

    @Autowired
    private PlanCheckErpDaoImpl planDao;

    @Autowired
    private PlanCheckRecordErpDaoImpl planRecordDao;

    @Autowired
    private ExportSessionDaoImpl exportSessionDao;

    public String sendProsecutorAck(final String requestId, final String description) {
        logger.info("{} : Start processing ProsecutorAsk message", requestId);
        final JAXBElement<RequestMsg> requestMessage = messageFactory.constructProsecutorAsk(requestId);
        if (requestMessage == null) {
            logger.error("{} : End. Error: message from Factory is NULL", requestId);
            return null;
        }
        return sendMessage(requestId, requestMessage, description, ProsecutorAskType.class.getSimpleName());
    }



    public String sendPlanRegular294Initialization(
            final String requestId,
            final String description,
            final CipCheckPlan checkPlan,
            final String acceptedName,
            final Integer year,
            final List<CipCheckPlanRecord> planRecords
    ) {
        final JAXBElement<RequestMsg> requestMessage = messageFactory.constructPlanRegular294Initialization(
                requestId,
                StringUtils.defaultString(acceptedName, checkPlan.getAcceptedName()),
                year != null ? year : checkPlan.getYear(),
                planRecords
        );
        if (requestMessage == null) {
            logger.error("{} : End. Error: message from Factory is NULL", requestId);
            return null;
        }
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }

        final String messageType = MessageToERP294Type.PlanRegular294Initialization.class.getSimpleName();
        final ExpSession exportSession = exportSessionDao.createExportSession(description, messageType, requestId);
        logger.info("{} : Created ExportSession: {}", requestId, exportSession);
        final ExpSessionEvent exportEvent = exportSessionDao.createExportEvent(messageType, exportSession);
        logger.info("{} : Created ExportEvent: {}", requestId, exportEvent);

        final PlanCheckErp planCheckErp = planDao.createPlanCheckErp(checkPlan, null, exportSession);
        logger.info("{} : Created PlanCheckErp: {}", requestId, planCheckErp);
        final List<PlanCheckRecErp> planCheckRecErpList = new ArrayList<>(planRecords.size());
        for (CipCheckPlanRecord record : planRecords) {
            final PlanCheckRecErp planCheckRecErp = planRecordDao.createPlanCheckRecErp(planCheckErp, record);
            planCheckRecErpList.add(planCheckRecErp);
            logger.info("{} : Created PlanCheckRecErp: {}", requestId, planCheckRecErp);
        }

        try {
            final String messageId = messageSender.send(result);
            logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            planDao.setStatus(planCheckErp, StatusErp.ERROR);
            planRecordDao.setStatus(planCheckRecErpList, StatusErp.ERROR);
            return null;
        }
        exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
        return result;
    }



    public String sendPlanRegular294Correction(
            final String requestId,
            final String description,
            final CipCheckPlan checkPlan,
            final BigInteger erpID,
            final String acceptedName,
            final int year,
            final List<CipCheckPlanRecord> planRecords,
            final Map<Integer, BigInteger> erpIDByCorrelatedID
    ) {
        final JAXBElement<RequestMsg> requestMessage = messageFactory.constructPlanRegular294Correction(
                StringUtils.defaultString(acceptedName),
                year,
                planRecords,
                erpID,
                erpIDByCorrelatedID,
                requestId
        );
        if (requestMessage == null) {
            logger.error("{} : End. Error: message from Factory is NULL", requestId);
            return null;
        }
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }

        final String messageType = MessageToERP294Type.PlanRegular294Correction.class.getSimpleName();
        final ExpSession exportSession = exportSessionDao.createExportSession(description, messageType, requestId);
        logger.info("{} : Created ExportSession: {}", requestId, exportSession);
        final ExpSessionEvent exportEvent = exportSessionDao.createExportEvent(messageType, exportSession);
        logger.info("{} : Created ExportEvent: {}", requestId, exportEvent);



        final PlanCheckErp planCheckErp = planDao.createPlanCheckErp(checkPlan, null, exportSession, erpID);
        logger.info("{} : Created PlanCheckErp: {}", requestId, planCheckErp);


        final List<PlanCheckRecErp> planCheckRecErpList = new ArrayList<>(planRecords.size());
        for (CipCheckPlanRecord record : planRecords) {
            final PlanCheckRecErp planCheckRecErp = planRecordDao.createPlanCheckRecErp(planCheckErp, record, erpIDByCorrelatedID.get(record.getCorrelationId()));
            planCheckRecErpList.add(planCheckRecErp);
            logger.info("{} : Created PlanCheckRecErp: {}", requestId, planCheckRecErp);
        }

        try {
            final String messageId = messageSender.send(result);
            logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            planDao.setStatus(planCheckErp, StatusErp.ERROR);
            planRecordDao.setStatus(planCheckRecErpList, StatusErp.ERROR);
            return null;
        }
        exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
        return result;
    }




    private <T> String sendMessage(
            final String requestId, final JAXBElement<T> requestMessage, final String description, final String messageType
    ) {
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final ExpSession exportSession = exportSessionDao.createExportSession(description, messageType, requestId);
        logger.info("{} : Created ExportSession: {}", requestId, exportSession);
        final ExpSessionEvent exportEvent = exportSessionDao.createExportEvent(messageType, exportSession);
        logger.info("{} : Created ExportEvent: {}", requestId, exportEvent);
        try {
            final String messageId = messageSender.send(result);
            logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            return null;
        }
        exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
        return result;
    }

}
