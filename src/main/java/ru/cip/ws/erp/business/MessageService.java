package ru.cip.ws.erp.business;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.factory.JAXBMarshallerUtil;
import ru.cip.ws.erp.factory.MessageFactory;
import ru.cip.ws.erp.generated.erptypes.MessageToERP294Type;
import ru.cip.ws.erp.generated.erptypes.ProsecutorAskType;
import ru.cip.ws.erp.generated.erptypes.RequestMsg;
import ru.cip.ws.erp.jdbc.dao.CheckPlanDaoImpl;
import ru.cip.ws.erp.jdbc.dao.CheckPlanRecordDaoImpl;
import ru.cip.ws.erp.jdbc.dao.ExportSessionDaoImpl;
import ru.cip.ws.erp.jdbc.entity.*;
import ru.cip.ws.erp.jms.MQMessageSender;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Upatov Egor <br>
 * Date: 18.09.2016, 14:24 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
public class MessageService {

    private final static Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageFactory messageFactory;
    @Autowired
    private MQMessageSender messageSender;
    @Autowired
    private CheckPlanDaoImpl checkPlanDao;
    @Autowired
    private CheckPlanRecordDaoImpl checkPlanRecordDao;
    @Autowired
    private ExportSessionDaoImpl exportSessionDao;

    public String sendPlanRegular294Correction(
            final String requestId,
            final CipCheckPlan checkPlan,
            final List<CipCheckPlanRecord> planRecords,
            final PlanCheckErp planCheckErp,
            final List<PlanCheckRecErp> planCheckRecErpList,
            final String acceptedName,
            final Integer year
    ) {
        final String messageType = MessageToERP294Type.PlanRegular294Correction.class.getSimpleName();
        logger.info("{} : Start construct PlanRegular294Correction message", requestId);
        final JAXBElement<RequestMsg> requestMessage = messageFactory.constructPlanRegular294Correction(
                StringUtils.defaultString(acceptedName, checkPlan.getAcceptedName()),
                year != null ? year : checkPlan.getYear(),
                planRecords,
                planCheckErp,
                planCheckRecErpList,
                requestId
        );
        if (requestMessage == null) {
            logger.error("{} : End. Error: message from Factory is NULL", requestId);
            return null;
        }
        logger.debug("{} : End construct PlanRegular294Correction message", requestId);
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final ExpSession exportSession = exportSessionDao.createExportSession(
                "4.1.3 Запрос на размещение корректировки плана плановых проверок",
                messageType,
                requestId
        );
        logger.info("{} : Created ExportSession: {}", requestId, exportSession);
        final ExpSessionEvent exportEvent = exportSessionDao.createExportEvent(messageType, exportSession);
        logger.info("{} : Created ExportEvent: {}", requestId, exportEvent);
        checkPlanDao.setExportSessionAndStatus(planCheckErp, StatusErp.WAIT_FOR_CORRECTION, exportSession);
        logger.info("{} : Update PlanCheckErp: {}", requestId, planCheckErp);
        for (CipCheckPlanRecord record : planRecords) {
            PlanCheckRecErp correlated = null;
            for (PlanCheckRecErp checkRecErp : planCheckRecErpList) {
                if (record.getCorrelationId().equals(checkRecErp.getCorrelationId())) {
                    correlated = checkRecErp;
                    break;
                }
            }
            if (correlated == null) {
                final PlanCheckRecErp planCheckRecErp = checkPlanRecordDao.createPlanCheckRecErp(planCheckErp, record);
                logger.info("{} : Create new PlanCheckRecErp: {}", requestId, planCheckRecErp);
            } else {
                checkPlanRecordDao.setStatus(correlated, StatusErp.WAIT_FOR_CORRECTION);
                logger.info("{} : Update PlanCheckRecErp: {}", requestId, correlated);
            }
        }
        final String messageId = messageSender.send(result);
        logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
        if (StringUtils.isEmpty(messageId)) {
            exportSessionDao.setSessionInfo(exportSession, "ERROR", "Not sent to JMS");
            checkPlanDao.setStatus(planCheckErp, StatusErp.ERROR);
            checkPlanRecordDao.setStatus(planCheckRecErpList, StatusErp.ERROR);
            return null;
        } else {
            exportSessionDao.setSessionStatus(exportSession, "DONE");
            return result;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String sendPlanRegular294Initialization(
            final String requestId,
            final CipCheckPlan checkPlan,
            final String acceptedName,
            final Integer year,
            final List<CipCheckPlanRecord> planRecords
    ) {
        final String messageType =  MessageToERP294Type.PlanRegular294Initialization.class.getSimpleName();
        logger.debug("{} : Start construct PlanRegular294Initialization message", requestId);
        final JAXBElement<RequestMsg> requestMessage = messageFactory.constructPlanRegular294Initialization(
                StringUtils.defaultString(acceptedName, checkPlan.getAcceptedName()),
                year != null ? year : checkPlan.getYear(),
                planRecords,
                requestId
        );
        if (requestMessage == null) {
            logger.error("{} : End. Error: message from Factory is NULL", requestId);
            return null;
        }
        logger.debug("{} : End construct PlanRegular294Initialization message", requestId);
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final ExpSession exportSession = exportSessionDao.createExportSession(
                "4.1.2 Запрос на первичное размещение плана плановых проверок",
               messageType,
                requestId
        );
        logger.info("{} : Created ExportSession: {}", requestId, exportSession);
        final ExpSessionEvent exportEvent = exportSessionDao.createExportEvent(messageType, exportSession);
        logger.info("{} : Created ExportEvent: {}", requestId, exportEvent);
        final PlanCheckErp planCheckErp = checkPlanDao.createPlanCheckErp(checkPlan, null, exportSession);
        logger.info("{} : Created PlanCheckErp: {}", requestId, planCheckErp);
        final List<PlanCheckRecErp> planCheckRecErpList = new ArrayList<>(planRecords.size());
        for (CipCheckPlanRecord record : planRecords) {
            final PlanCheckRecErp planCheckRecErp = checkPlanRecordDao.createPlanCheckRecErp(planCheckErp, record);
            planCheckRecErpList.add(planCheckRecErp);
            logger.info("{} : Created PlanCheckRecErp: {}", requestId, planCheckRecErp);
        }
        try {
            final String messageId = messageSender.send(result);
            logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, "ERROR", "Not sent to JMS: " + e.getMessage());
            checkPlanDao.setStatus(planCheckErp, StatusErp.ERROR);
            checkPlanRecordDao.setStatus(planCheckRecErpList, StatusErp.ERROR);
            return null;
        }
        exportSessionDao.setSessionStatus(exportSession, "DONE");
        return result;
    }

    public String sendProsecutorAck(final String requestId) {
        logger.info("{} : Start processing ProsecutorAsk message", requestId);
        final JAXBElement<RequestMsg> requestMessage = messageFactory.constructProsecutorAsk(requestId);
        if (requestMessage == null) {
            logger.error("{} : End. Error: message from Factory is NULL", requestId);
            return null;
        }
        return sendMessage(
                requestId,
                requestMessage,
                "4.1.1 Запрос на получение справочника территориальных юрисдикций прокуратур Российской Федерации",
                ProsecutorAskType.class.getSimpleName()
        );
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
            exportSessionDao.setSessionInfo(exportSession, "ERROR", "Not sent to JMS: " + e.getMessage());
            return null;
        }
        exportSessionDao.setSessionStatus(exportSession, "DONE");
        return result;
    }

}
