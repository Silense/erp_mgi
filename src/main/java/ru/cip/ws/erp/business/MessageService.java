package ru.cip.ws.erp.business;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.factory.JAXBMarshallerUtil;
import ru.cip.ws.erp.factory.MessageFactory;
import ru.cip.ws.erp.generated.erptypes.MessageToERPModelType;
import ru.cip.ws.erp.generated.erptypes.RequestMsg;
import ru.cip.ws.erp.jms.MQMessageSender;
import ru.cip.ws.erp.jpa.dao.ExportSessionDaoImpl;
import ru.cip.ws.erp.jpa.dao.PlanErpDaoImpl;
import ru.cip.ws.erp.jpa.dao.Tuple;
import ru.cip.ws.erp.jpa.dao.UplanErpDaoImpl;
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
import java.util.Map;
import java.util.Set;

/**
 * Author: Upatov Egor <br>
 * Date: 18.09.2016, 14:24 <br>
 * Description: Сервис для обработки и отправки сообщений (как эталонных, так и нет)
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
    private ExportSessionDaoImpl expSessionDao;


    public String sendProsecutorAck(final String uuid, final String description) {
        final DataKindEnum dataKind = DataKindEnum.PROSECUTOR_ACK;
        logger.info("{} : Start processing ProsecutorAsk message", uuid);
        final JAXBElement<RequestMsg> prosecutorAsk = MessageFactory.createProsecutorAsk(uuid);
        final String result = JAXBMarshallerUtil.marshalAsString(prosecutorAsk, uuid);
        logger.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
        logTuple(uuid, tupleExpSession, logger);
        try {
            messageSender.send(result);
            expSessionDao.setSessionStatus(tupleExpSession.left, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            expSessionDao.setSessionInfo(tupleExpSession.left, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            return null;
        }
    }


    public String sendPlanRegular294Initialization(
            final String uuid,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Plan plan,
            final String acceptedName,
            final Integer year,
            final Set<PlanRecord> records
    ) {
        final DataKindEnum dataKind = DataKindEnum.PLAN_REGULAR_294_INITIALIZATION;
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createPlanRegular294Initialization(
                uuid,
                mailer,
                addressee,
                KO_NAME,
                StringUtils.defaultString(acceptedName, plan.getAcceptedName()),
                year != null ? year : plan.getYear(),
                records
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, uuid);
        logger.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
        final Tuple<PlanErp, Set<PlanRecErp>> tupleErp = planDao.createErp(plan, null, records, null, null, dataKind, tupleExpSession.left);
        logPlanTuples(uuid, tupleExpSession, tupleErp, logger);
        try {
            messageSender.send(result);
            expSessionDao.setSessionStatus(tupleExpSession.left, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            logger.error("{} : Error while sending JMS message", uuid, e);
            final String errorMessage = "Not sent to JMS: " + e.getMessage();
            expSessionDao.setSessionInfo(tupleExpSession.left, SessionStatus.ERROR, errorMessage);
            planDao.setStatus(StatusErp.ERROR, tupleErp.left, tupleErp.right, errorMessage);
            return null;
        }
    }


    public String sendUplanUnregular294Initialization(
            final String uuid,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Uplan uplan,
            final Set<UplanRecord> records
    ) {
        final DataKindEnum dataKind = DataKindEnum.UPLAN_UNREGULAR_294_INITIALIZATION;
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createUplanUnregular294Initialization(
                uuid, mailer, addressee, KO_NAME, uplan, records
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, uuid);
        logger.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
        final Tuple<UplanErp, Set<UplanRecErp>> tupleErp = uplanDao.createErp(uplan, null, records, null, null, dataKind, tupleExpSession.left);
        logPlanTuples(uuid, tupleExpSession, tupleErp, logger);
        try {
            messageSender.send(result);
            expSessionDao.setSessionStatus(tupleExpSession.left, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            logger.error("{} : Error while sending JMS message", uuid, e);
            final String errorMessage = "Not sent to JMS: " + e.getMessage();
            expSessionDao.setSessionInfo(tupleExpSession.left, SessionStatus.ERROR, errorMessage);
            uplanDao.setStatus(StatusErp.ERROR, tupleErp.left, tupleErp.right, errorMessage);
            return null;
        }
    }


    public String sendPlanRegular294Correction(
            final String uuid,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Plan plan,
            final BigInteger erpID,
            final String acceptedName,
            final int year,
            final Set<PlanRecord> records,
            final Map<Long, BigInteger> erpIDMap
    ) {
        final DataKindEnum dataKind = DataKindEnum.PLAN_REGULAR_294_CORRECTION;
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createPlanRegular294Correction(
                uuid, mailer, addressee, KO_NAME, StringUtils.defaultString(acceptedName), year, records, erpID, erpIDMap
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, uuid);
        logger.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
        final Tuple<PlanErp, Set<PlanRecErp>> tupleErp = planDao.createErp(plan, erpID, records, erpIDMap, null, dataKind, tupleExpSession.left);
        logPlanTuples(uuid, tupleExpSession, tupleErp, logger);
        try {
            messageSender.send(result);
            expSessionDao.setSessionStatus(tupleExpSession.left, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            logger.error("{} : Error while sending JMS message", uuid, e);
            final String errorMessage = "Not sent to JMS: " + e.getMessage();
            expSessionDao.setSessionInfo(tupleExpSession.left, SessionStatus.ERROR, errorMessage);
            planDao.setStatus(StatusErp.ERROR, tupleErp.left, tupleErp.right, errorMessage);
            return null;
        }
    }

    public String sendUplanUnregular294Correction(
            final String uuid,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Uplan uplan,
            final BigInteger id,
            final Set<UplanRecord> records,
            final Map<Long, BigInteger> erpIDMap
    ) {
        final DataKindEnum dataKind = DataKindEnum.UPLAN_UNREGULAR_294_CORRECTION;
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createUplanUnregular294Correction(
                uuid, mailer, addressee, KO_NAME, uplan, id, records, erpIDMap
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, uuid);
        logger.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
        final Tuple<UplanErp, Set<UplanRecErp>> tupleErp = uplanDao.createErp(uplan, id, records, erpIDMap, null, dataKind, tupleExpSession.left);
        logPlanTuples(uuid, tupleExpSession, tupleErp, logger);
        try {
            messageSender.send(result);
            expSessionDao.setSessionStatus(tupleExpSession.left, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            logger.error("{} : Error while sending JMS message", uuid, e);
            final String errorMessage = "Not sent to JMS: " + e.getMessage();
            expSessionDao.setSessionInfo(tupleExpSession.left, SessionStatus.ERROR, errorMessage);
            uplanDao.setStatus(StatusErp.ERROR, tupleErp.left, tupleErp.right, errorMessage);
            return null;
        }
    }

    public String sendPlanResult294Initialization(
            final String uuid,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Plan plan,
            final BigInteger erpID,
            final int year,
            final Map<PlanAct, Set<PlanActViolation>> actMap,
            final Map<Long, BigInteger> erpIDMap
    ) {
        final DataKindEnum dataKind = DataKindEnum.PLAN_RESULT_294_INITIALIZATION;
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createPlanResult294Initialization(
                uuid, mailer, addressee, year, actMap, erpID, erpIDMap
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, uuid);
        logger.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
        final Tuple<PlanErp, Set<PlanRecErp>> tupleErp = planDao.createErp(plan, erpID, actMap, erpIDMap, null, dataKind, tupleExpSession.left);
        logPlanTuples(uuid, tupleExpSession, tupleErp, logger);
        try {
            messageSender.send(result);
            expSessionDao.setSessionStatus(tupleExpSession.left, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            logger.error("{} : Error while sending JMS message", uuid, e);
            final String errorMessage = "Not sent to JMS: " + e.getMessage();
            expSessionDao.setSessionInfo(tupleExpSession.left, SessionStatus.ERROR, errorMessage);
            planDao.setStatus(StatusErp.ERROR, tupleErp.left, tupleErp.right, errorMessage);
            return null;
        }
    }

    public String sendUplanResult294Initialization(
            final String uuid,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final BigInteger erpID,
            final int year,
            final Map<UplanAct, Set<UplanActViolation>> actMap
    ) {
        final DataKindEnum dataKind = DataKindEnum.UPLAN_RESULT_294_INITIALIZATION;
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createUplanResult294Initialization(
                uuid, mailer, addressee, year, actMap, erpID
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, uuid);
        logger.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
        logTuple(uuid, tupleExpSession, logger);
        //TODO
        try {
            messageSender.send(result);
            expSessionDao.setSessionStatus(tupleExpSession.left, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            expSessionDao.setSessionInfo(tupleExpSession.left, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            return null;
        }

    }

    public String sendPlanResult294Correction(
            final String uuid,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final Plan plan,
            final BigInteger erpID,
            final int year,
            final Map<PlanAct, Set<PlanActViolation>> actMap,
            final Map<Long, BigInteger> erpIDMap
    ) {
        final DataKindEnum dataKind = DataKindEnum.PLAN_RESULT_294_CORRECTION;
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createPlanResult294Correction(
                uuid, mailer, addressee, year, actMap, erpID, erpIDMap
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, uuid);
        logger.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
        final Tuple<PlanErp, Set<PlanRecErp>> tupleErp = planDao.createErp(plan, erpID, actMap, erpIDMap, null, dataKind, tupleExpSession.left);
        logPlanTuples(uuid, tupleExpSession, tupleErp, logger);
        try {
            messageSender.send(result);
            expSessionDao.setSessionStatus(tupleExpSession.left, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            logger.error("{} : Error while sending JMS message", uuid, e);
            final String errorMessage = "Not sent to JMS: " + e.getMessage();
            expSessionDao.setSessionInfo(tupleExpSession.left, SessionStatus.ERROR, errorMessage);
            planDao.setStatus(StatusErp.ERROR, tupleErp.left, tupleErp.right, errorMessage);
            return null;
        }
    }

    private <A,B> void  logPlanTuples(
            final String uuid,
            final Tuple<ExpSession, ExpSessionEvent> tupleExpSession,
            final Tuple<A, Set<B>> tupleErp,
            final Logger logger
    ) {
        if(logger.isDebugEnabled()){
            logger.debug("{} : Created ExportSession: {}", uuid, tupleExpSession.left);
            logger.debug("{} : Created ExportEvent: {}", uuid, tupleExpSession.right);
            logger.debug("{} : Created[main plan]: {}", uuid, tupleErp.left);
            for (Object x : tupleErp.right) {
                logger.debug("{} : Created[record]: {}", uuid, x);
            }
        }
    }

    private <A,B> void logTuple(final String uuid, final Tuple<A, B> tuple,  final Logger logger){
        logger.debug("{} : Created[L]: {}", uuid, tuple.left);
        logger.debug("{} : Created[R]: {}", uuid, tuple.right);
    }

}
