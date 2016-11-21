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
import ru.cip.ws.erp.generated.erptypes.MessageToERPModelType;
import ru.cip.ws.erp.generated.erptypes.ProsecutorAskType;
import ru.cip.ws.erp.generated.erptypes.RequestMsg;
import ru.cip.ws.erp.jpa.dao.ExportSessionDaoImpl;
import ru.cip.ws.erp.jpa.dao.PlanErpDaoImpl;
import ru.cip.ws.erp.jpa.dao.PlanRecordErpDaoImpl;
import ru.cip.ws.erp.jpa.entity.*;
import ru.cip.ws.erp.jpa.entity.enums.SessionStatus;
import ru.cip.ws.erp.jpa.entity.enums.StatusErp;
import ru.cip.ws.erp.jpa.entity.sessions.ExpSession;
import ru.cip.ws.erp.jpa.entity.sessions.ExpSessionEvent;
import ru.cip.ws.erp.jpa.entity.views.*;
import ru.cip.ws.erp.jms.MQMessageSender;
import ru.cip.ws.erp.servlet.DataKindEnum;

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
    private MQMessageSender messageSender;

    @Autowired
    private PlanErpDaoImpl planDao;

    @Autowired
    private PlanRecordErpDaoImpl planRecordDao;

    @Autowired
    private ExportSessionDaoImpl exportSessionDao;

    public String sendProsecutorAck(final String requestId, final String description) {
        logger.info("{} : Start processing ProsecutorAsk message", requestId);
        return sendMessage(requestId, MessageFactory.constructProsecutorAsk(requestId), description, ProsecutorAskType.class.getSimpleName());
    }


    public String sendPlanRegular294Initialization(
            final String requestId,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Plan plan,
            final String acceptedName,
            final Integer year,
            final List<PlanRecord> planRecords
    ) {
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.constructPlanRegular294Initialization(
                requestId,
                mailer,
                addressee,
                KO_NAME,
                StringUtils.defaultString(acceptedName, plan.getAcceptedName()),
                year != null ? year : plan.getYear(),
                planRecords
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }

        final ExpSession exportSession = createExportInfo(
                requestId,
                description,
                MessageToERP294Type.PlanRegular294Initialization.class.getSimpleName()
        );

        final PlanErp planErp = planDao.createPlanErp(plan, null, DataKindEnum.PLAN_REGULAR_294_INITIALIZATION, exportSession);
        logger.info("{} : Created PlanErp: {}", requestId, planErp);
        final List<PlanRecErp> planRecErpList = new ArrayList<>(planRecords.size());
        for (PlanRecord record : planRecords) {
            final PlanRecErp planRecErp = planRecordDao.createPlanRecErp(planErp, record);
            planRecErpList.add(planRecErp);
            logger.info("{} : Created PlanRecErp: {}", requestId, planRecErp);
        }

        try {
            final String messageId = messageSender.send(result);
            logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            planDao.setStatus(planErp, StatusErp.ERROR);
            planRecordDao.setStatus(planRecErpList, StatusErp.ERROR);
            return null;
        }
        exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
        return result;
    }

    private ExpSession createExportInfo(final String requestId, final String description, final String messageType) {
        final ExpSession result = exportSessionDao.createExportSession(description, messageType, requestId);
        logger.info("{} : Created ExportSession: {}", requestId, result);
        final ExpSessionEvent exportEvent = exportSessionDao.createExportEvent(messageType, result);
        logger.info("{} : Created ExportEvent: {}", requestId, exportEvent);
        return result;
    }

    public String setUplanUnregular294Initialization(
            final String requestId,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Uplan uplan,
            final List<UplanRecord> addressList
    ) {
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.constructUplanUnregular294Initialization(
                requestId, mailer, addressee, KO_NAME, uplan, addressList
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final ExpSession exportSession = createExportInfo(
                requestId,
                description,
                MessageToERP294Type.UplanResult294Initialization.class.getSimpleName()
        );
        //TODO
        try {
            final String messageId = messageSender.send(result);
            logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
//            planDao.setStatus(planErp, StatusErp.ERROR);
//            planRecordDao.setStatus(planRecErpList, StatusErp.ERROR); TODO
            return null;
        }
        exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
        return result;
    }


    public String sendPlanRegular294Correction(
            final String requestId,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Plan plan,
            final BigInteger erpID,
            final String acceptedName,
            final int year,
            final List<PlanRecord> planRecords,
            final Map<Integer, BigInteger> erpIDByCorrelatedID
    ) {
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.constructPlanRegular294Correction(
                requestId, mailer, addressee, KO_NAME, StringUtils.defaultString(acceptedName), year, planRecords, erpID, erpIDByCorrelatedID
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final ExpSession exportSession = createExportInfo(requestId, description, MessageToERP294Type.PlanRegular294Correction.class.getSimpleName());

        final PlanErp planErp = planDao.createPlanErp(plan, null, DataKindEnum.PLAN_REGULAR_294_CORRECTION, exportSession, erpID);
        logger.info("{} : Created PlanErp: {}", requestId, planErp);

        final List<PlanRecErp> planRecErpList = new ArrayList<>(planRecords.size());
        for (PlanRecord record : planRecords) {
            final PlanRecErp planRecErp = planRecordDao.createPlanRecErp(
                    planErp, record, erpIDByCorrelatedID.get(record.getCorrelationId())
            );
            planRecErpList.add(planRecErp);
            logger.info("{} : Created PlanRecErp: {}", requestId, planRecErp);
        }

        try {
            final String messageId = messageSender.send(result);
            logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            planDao.setStatus(planErp, StatusErp.ERROR);
            planRecordDao.setStatus(planRecErpList, StatusErp.ERROR);
            return null;
        }
        exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
        return result;
    }

    public String setUplanUnregular294Correction(
            final String requestId,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Uplan uplan,
            final BigInteger id,
            final List<UplanRecord> addressList,
            final Map<Long, BigInteger> erpIDByCorrelatedID
    ) {

        final JAXBElement<RequestMsg> requestMessage = MessageFactory.constructUplanUnregular294Correction(
                requestId, mailer, addressee, KO_NAME, uplan, id, addressList, erpIDByCorrelatedID
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final ExpSession exportSession = createExportInfo(
                requestId,
                description,
                MessageToERP294Type.UplanResult294Initialization.class.getSimpleName()
        );
        //TODO
        try {
            final String messageId = messageSender.send(result);
            logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
//            planDao.setStatus(planErp, StatusErp.ERROR);
//            planRecordDao.setStatus(planRecErpList, StatusErp.ERROR); TODO
            return null;
        }
        exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
        return result;
    }

    public String sendPlanResult294Initialization(
            final String requestId,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Plan plan,
            final BigInteger erpID,
            final int year,
            final Map<PlanAct, List<PlanActViolation>> actMap,
            final Map<Integer, BigInteger> erpIDByCorrelatedID
    ) {
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.constructPlanResult294Initialization(
                requestId, mailer, addressee, year, actMap, erpID, erpIDByCorrelatedID
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }

        final ExpSession exportSession =  createExportInfo(requestId, description, MessageToERP294Type.PlanResult294Initialization.class.getSimpleName());

        final PlanErp planErp = planDao.createPlanErp(plan, null, DataKindEnum.PLAN_RESULT_294_INITIALIZATION, exportSession, erpID);
        logger.info("{} : Created PlanErp: {}", requestId, planErp);

        final List<PlanRecErp> planRecErpList = new ArrayList<>(actMap.size());
        for (PlanAct record : actMap.keySet()) {
            final PlanRecErp planRecErp = planRecordDao.createPlanRecErp(
                    planErp, record.getCorrelationID(), erpIDByCorrelatedID.get(record.getCorrelationID())
            );
            planRecErpList.add(planRecErp);
            logger.info("{} : Created PlanRecErp: {}", requestId, planRecErp);
        }

        try {
            final String messageId = messageSender.send(result);
            logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            planDao.setStatus(planErp, StatusErp.ERROR);
            planRecordDao.setStatus(planRecErpList, StatusErp.ERROR);
            return null;
        }
        exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
        return result;
    }

    public String sendPlanResult294Correction(
            final String requestId,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final Plan plan,
            final BigInteger erpID,
            final int year,
            final Map<PlanAct, List<PlanActViolation>> actMap,
            final Map<Integer, BigInteger> erpIDByCorrelatedID
    ) {
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.constructPlanResult294Correction(
                requestId, mailer, addressee, year, actMap, erpID, erpIDByCorrelatedID
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }

        final ExpSession exportSession = createExportInfo(requestId, description, MessageToERP294Type.PlanResult294Correction.class.getSimpleName());

        final PlanErp planErp = planDao.createPlanErp(plan, null, DataKindEnum.PLAN_RESULT_294_CORRECTION, exportSession, erpID);
        logger.info("{} : Created PlanErp: {}", requestId, planErp);


        final List<PlanRecErp> planRecErpList = new ArrayList<>(actMap.size());
        for (PlanAct record : actMap.keySet()) {
            final PlanRecErp planRecErp = planRecordDao.createPlanRecErp(
                    planErp, record.getCorrelationID(), erpIDByCorrelatedID.get(record.getCorrelationID())
            );
            planRecErpList.add(planRecErp);
            logger.info("{} : Created PlanRecErp: {}", requestId, planRecErp);
        }

        try {
            final String messageId = messageSender.send(result);
            logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            planDao.setStatus(planErp, StatusErp.ERROR);
            planRecordDao.setStatus(planRecErpList, StatusErp.ERROR);
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
        final ExpSession exportSession = createExportInfo(requestId, description, messageType);
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
