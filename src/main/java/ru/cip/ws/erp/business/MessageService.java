package ru.cip.ws.erp.business;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
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
import java.util.UUID;

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

    public String sendPlanRegular294initialization(
            final CipCheckPlan checkPlan, final String acceptedName, final Integer year, final List<CipCheckPlanRecord> planRecords
    ) {
        final String requestId = UUID.randomUUID().toString();
        logger.info("{} : Start processing ProsecutorAsk message", requestId);
        final JAXBElement<RequestMsg> requestMessage = messageFactory.constructPlanRegular294initialization(
                StringUtils.defaultString(acceptedName, checkPlan.getACCEPTED_NAME()),
                year != null ? year : checkPlan.getYEAR(),
                planRecords,
                requestId
        );
        if (requestMessage == null) {
            logger.error("{} : End. Error: message from Factory is NULL", requestId);
            return null;
        }
        final String description = "4.1.2 Запрос на первичное размещение плана плановых проверок";
        final String messageType = MessageToERP294Type.PlanRegular294Initialization.class.getSimpleName();

        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        if (StringUtils.isEmpty(result)) {
            return null;
        }

        final ExpSession exportSession = exportSessionDao.createExportSession(description, messageType, requestId);
        logger.info("{} : Create new ExportSession: {}", requestId, exportSession);
        final ExpSessionEvent exportEvent = exportSessionDao.createExportEvent(result, exportSession);
        logger.info("{} : Create new ExportEvent: {}", requestId, exportEvent);
        final PlanCheckErp planCheckErp = checkPlanDao.createPlanCheckErp(checkPlan.getCHECK_PLAN_ID(), null, exportSession);
        logger.info("{} : Create new PlanCheckErp: {}", requestId, planCheckErp);
        final List<PlanCheckRecErp> planCheckRecErpList = new ArrayList<>(planRecords.size());
        for (CipCheckPlanRecord record : planRecords) {
            final PlanCheckRecErp planCheckRecErp = checkPlanRecordDao.createPlanCheckRecErp(planCheckErp, record);
            planCheckRecErpList.add(planCheckRecErp);
            logger.info("{} : Create new PlanCheckRecErp: {}", requestId, planCheckRecErp);
        }
        final String messageId = messageSender.send(result);
        logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
        if (StringUtils.isEmpty(messageId)) {
            exportSessionDao.setSessionInfo(exportSession, "ERROR", "Not sent to JMS");
            checkPlanDao.setStatus(planCheckErp, "ERROR");
            checkPlanRecordDao.setStatus(planCheckRecErpList, "ERROR");
            return null;
        } else {
            exportSessionDao.setSessionStatus(exportSession, "DONE");
            return result;
        }
    }

    public String sendProsecutorAck() {
        final String requestId = UUID.randomUUID().toString();
        logger.info("{} : Start processing ProsecutorAsk message", requestId);
        final JAXBElement<RequestMsg> requestMessage = messageFactory.constructProsecutorAsk(requestId);
        if (requestMessage == null) {
            logger.error("{} : End. Error: message from Factory is NULL", requestId);
            return null;
        }
        final String description = "4.1.1 Запрос на получение справочника территориальных юрисдикций прокуратур Российской Федерации";
        final String messageType = ProsecutorAskType.class.getSimpleName();
        return sendMessage(requestId, requestMessage, description, messageType);
    }

    private <T> String sendMessage(
            final String requestId, final JAXBElement<T> requestMessage, final String description, final String messageType
    ) {
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        if (!StringUtils.isEmpty(result)) {
            final ExpSession exportSession = exportSessionDao.createExportSession(description, messageType, requestId);
            logger.info("{} : Create new ExportSession: {}", requestId, exportSession);
            final ExpSessionEvent exportEvent = exportSessionDao.createExportEvent(result, exportSession);
            logger.info("{} : Create new ExportEvent: {}", requestId, exportEvent);
            final String messageId = messageSender.send(result);
            logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
            if (StringUtils.isEmpty(messageId)) {
                exportSessionDao.setSessionInfo(exportSession, "ERROR", "Not sent to JMS");
                return null;
            } else {
                exportSessionDao.setSessionInfo(exportSession, "DONE", "Sent to JMS");
                return result;
            }
        }
        return null;
    }


}
