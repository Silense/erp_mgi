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
import ru.cip.ws.erp.jms.MQMessageSender;
import ru.cip.ws.erp.jpa.dao.*;
import ru.cip.ws.erp.jpa.entity.PlanErp;
import ru.cip.ws.erp.jpa.entity.PlanRecErp;
import ru.cip.ws.erp.jpa.entity.UplanErp;
import ru.cip.ws.erp.jpa.entity.UplanRecErp;
import ru.cip.ws.erp.jpa.entity.enums.SessionStatus;
import ru.cip.ws.erp.jpa.entity.enums.StatusErp;
import ru.cip.ws.erp.jpa.entity.sessions.ExpSession;
import ru.cip.ws.erp.jpa.entity.sessions.ExpSessionEvent;
import ru.cip.ws.erp.jpa.entity.views.*;
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
    private UplanErpDaoImpl uplanDao;

    @Autowired
    private PlanRecordErpDaoImpl planRecordDao;

    @Autowired
    private ExportSessionDaoImpl exportSessionDao;

    private ExpSession createExportInfo(final String requestId, final String description, final String messageType) {
        final ExpSession result = exportSessionDao.createExportSession(description, messageType, requestId);
        logger.info("{} : Created ExportSession: {}", requestId, result);
        final ExpSessionEvent exportEvent = exportSessionDao.createExportEvent(messageType, result);
        logger.info("{} : Created ExportEvent: {}", requestId, exportEvent);
        return result;
    }

    public String sendProsecutorAck(final String requestId, final String description) {
        logger.info("{} : Start processing ProsecutorAsk message", requestId);
        final JAXBElement<RequestMsg> prosecutorAsk = MessageFactory.createProsecutorAsk(requestId);
        final String result = JAXBMarshallerUtil.marshalAsString(prosecutorAsk, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final ExpSession exportSession = createExportInfo(requestId, description, ProsecutorAskType.class.getSimpleName());
        try {
            messageSender.send(result);
            exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            return null;
        }
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
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createPlanRegular294Initialization(
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
                requestId, description, MessageToERP294Type.PlanRegular294Initialization.class.getSimpleName()
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
            messageSender.send(result);
            exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            planDao.setStatus(planErp, StatusErp.ERROR);
            planRecordDao.setStatus(planRecErpList, StatusErp.ERROR);
            return null;
        }
    }


    public String sendUplanUnregular294Initialization(
            final String requestId,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Uplan uplan,
            final List<UplanRecord> uplanRecords
    ) {
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createUplanUnregular294Initialization(
                requestId, mailer, addressee, KO_NAME, uplan, uplanRecords
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final ExpSession exportSession = createExportInfo(
                requestId, description, MessageToERP294Type.UplanUnregular294Initialization.class.getSimpleName()
        );
        final Tuple<UplanErp, List<UplanRecErp>> uplanErpTuple = uplanDao.createErp(
                requestId, uplan, uplanRecords, null, DataKindEnum.UPLAN_UNREGULAR_294_INITIALIZATION, exportSession
        );
        try {
            messageSender.send(result);
            exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            logger.error("{} : Error while sending JMS message", requestId, e);
            uplanDao.setErrorStatus(uplanErpTuple, "Not sent to JMS: " + e.getMessage());
            return null;
        }
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
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createPlanRegular294Correction(
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
            messageSender.send(result);
            exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            planDao.setStatus(planErp, StatusErp.ERROR);
            planRecordDao.setStatus(planRecErpList, StatusErp.ERROR);
            return null;
        }
    }

    public String sendUplanUnregular294Correction(
            final String requestId,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Uplan uplan,
            final BigInteger id,
            final List<UplanRecord> uplanRecords,
            final Map<Long, BigInteger> erpIDByCorrelatedID
    ) {
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createUplanUnregular294Correction(
                requestId, mailer, addressee, KO_NAME, uplan, id, uplanRecords, erpIDByCorrelatedID
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final ExpSession exportSession = createExportInfo(
                requestId, description, MessageToERP294Type.UplanUnregular294Correction.class.getSimpleName()
        );
        final Tuple<UplanErp, List<UplanRecErp>> uplanErpTuple = uplanDao.createErp(
                requestId, uplan, id, uplanRecords, erpIDByCorrelatedID, null, DataKindEnum.UPLAN_UNREGULAR_294_CORRECTION, exportSession
        );
        try {
            messageSender.send(result);
            exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            logger.error("{} : Error while sending JMS message", requestId, e);
            uplanDao.setErrorStatus(uplanErpTuple, "Not sent to JMS: " + e.getMessage());
            return null;
        }
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
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createPlanResult294Initialization(
                requestId, mailer, addressee, year, actMap, erpID, erpIDByCorrelatedID
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }

        final ExpSession exportSession = createExportInfo(
                requestId, description, MessageToERP294Type.PlanResult294Initialization.class.getSimpleName()
        );

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
            messageSender.send(result);
            exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            planDao.setStatus(planErp, StatusErp.ERROR);
            planRecordDao.setStatus(planRecErpList, StatusErp.ERROR);
            return null;
        }
    }

    public String sendUplanResult294Initialization(
            final String requestId,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final BigInteger erpID,
            final int year,
            final Map<UplanAct, List<UplanActViolation>> actMap
    ) {
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createUplanResult294Initialization(
                requestId, mailer, addressee, year, actMap, erpID
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final ExpSession exportSession = createExportInfo(
                requestId, description, MessageToERP294Type.UplanResult294Initialization.class.getSimpleName()
        );
        //TODO
        try {
            messageSender.send(result);
            exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            return null;
        }

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
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createPlanResult294Correction(
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
            messageSender.send(result);
            exportSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            planDao.setStatus(planErp, StatusErp.ERROR);
            planRecordDao.setStatus(planRecErpList, StatusErp.ERROR);
            return null;
        }
    }
}
