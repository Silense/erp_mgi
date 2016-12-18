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
import ru.cip.ws.erp.jpa.dao.CheckErpDaoImpl;
import ru.cip.ws.erp.jpa.dao.EnumDaoImpl;
import ru.cip.ws.erp.jpa.dao.ExportSessionDaoImpl;
import ru.cip.ws.erp.jpa.dao.Tuple;
import ru.cip.ws.erp.jpa.entity.CheckErp;
import ru.cip.ws.erp.jpa.entity.CheckHistory;
import ru.cip.ws.erp.jpa.entity.CheckRecordErp;
import ru.cip.ws.erp.jpa.entity.enums.SessionStatus;
import ru.cip.ws.erp.jpa.entity.sessions.ExpSession;
import ru.cip.ws.erp.jpa.entity.sessions.ExpSessionEvent;
import ru.cip.ws.erp.jpa.entity.views.Plan;
import ru.cip.ws.erp.jpa.entity.views.PlanRecord;
import ru.cip.ws.erp.jpa.entity.views.Uplan;
import ru.cip.ws.erp.jpa.entity.views.UplanRecord;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBElement;
import java.util.*;

/**
 * Author: Upatov Egor <br>
 * Date: 18.09.2016, 14:24 <br>
 * Description: Сервис для обработки и отправки сообщений (как эталонных, так и нет)
 */
@Repository
@Transactional
public class MessageService {

    private final static Logger log = LoggerFactory.getLogger(MessageService.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MQMessageSender messageSender;

    @Autowired
    private ExportSessionDaoImpl expSessionDao;

    @Autowired
    private CheckErpDaoImpl checkErpDao;

    @Autowired
    private EnumDaoImpl enumDao;

    public String sendProsecutorAck(final long requestNumber, final String description) {
        final String uuid = UUID.randomUUID().toString();
        log.info("#{} : assigned UUID='{}'", requestNumber, uuid);
        final Date date = new Date();
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(date, description, "PROSECUTOR_ASK", 1);
        logTuple(requestNumber, tupleExpSession);
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createProsecutorAsk(uuid);
        try {
            final String result = messageSender.send(requestNumber, requestMessage);
            expSessionDao.setSessionStatus(tupleExpSession.left, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            expSessionDao.setSessionInfo(tupleExpSession.left, SessionStatus.ERROR, "Not sent to JMS: " + e.getMessage());
            return null;
        }
    }


    /**
     * Произвести массовое размещение Внеплановых проверок в ЕРП
     *
     * @param logTag      префикс-номер для логиирования
     * @param description Общее пояснение к размещению проверок (для EXP_SESSION)
     * @param mailer      от имени какой структуры будем размещать
     * @param addressee   на имя какой структуры будем размещать
     * @param KO_NAME
     * @param checks      карта проверок и объектов проверки
     * @return карта результатов размещения <Идентификатор проверки, Результат размещения>,
     * где результат размещения- номер requestId для ЕРП в случае успешной отправки
     * или текстовое пояснение почему проверка не была размещена
     */
    public Map<String, String> processUnregularAllocationBatch(
            final long logTag,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Map<Uplan, Set<UplanRecord>> checks
    ) {
        log.info("#{} : START Unregular AllocationBatch [{} checks]", logTag, checks.size());
        if (checks.isEmpty()) {
            log.warn("#{} : END. Nothing to send. (Empty checks map)", logTag);
            return new LinkedHashMap<>(0);
        }
        final Date date = new Date();
        final ExpSession exportSession = expSessionDao.createExportSession(date, description, "UPLAN_UNREGULAR_294_ALLOCATION", checks.size());
        log.info("#{} : Created {}", logTag, exportSession);

        final Map<String, String> result = new LinkedHashMap<>(checks.size());
        for (Map.Entry<Uplan, Set<UplanRecord>> entry : checks.entrySet()) {
            final Uplan uplan = entry.getKey();
            final Set<UplanRecord> records = entry.getValue();
            final String requestNumber = "#" + logTag + "-" + uplan.getCHECK_ID();
            final String sendResult = processUnregularAllocation(requestNumber, mailer, addressee, KO_NAME, exportSession, uplan, records);
            result.put(String.valueOf(uplan.getCHECK_ID()), sendResult);
        }
        expSessionDao.setSessionStatus(exportSession, SessionStatus.DONE);
        return result;
    }

    public String processUnregularAllocation(
            final String requestNumber,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final ExpSession exportSession,
            final Uplan uplan,
            final Set<UplanRecord> records
    ) {
        final String uuid = UUID.randomUUID().toString();
        log.info("{} : start processing UnregularAllocation for CHECK[{}]-'{}'", requestNumber, uplan.getCHECK_ID(), uuid);
        Tuple<CheckErp, Set<CheckRecordErp>> checkErpTuple = checkErpDao.getCheckErp(uplan.getCHECK_ID(), "UNREGULAR");
        String result;
        if (checkErpTuple == null) {
            //До этого не выгружали => Проводим первичное размещение
            checkErpTuple = checkErpDao.createErp(uplan, records, uuid);
            result = processUplanUnregular294Initialization(requestNumber,
                                                            exportSession,
                                                            uuid,
                                                            mailer,
                                                            addressee,
                                                            KO_NAME,
                                                            uplan,
                                                            records,
                                                            checkErpTuple.left
            );
        } else {
            //TODO Уже выгружали  - пробуем переотправитть\ скорректировать
           result = "Already processing by ERP";
        }
        log.info("{} : end. Result = '{}'", requestNumber, result);
        return result;
    }

    public String processUplanUnregular294Initialization(
            final String requestNumber,
            final ExpSession exportSession,
            final String uuid,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Uplan uplan,
            final Set<UplanRecord> records,
            final CheckErp checkErp
    ) {
        final Date eventDate = new Date();
        final ExpSessionEvent exportEvent = expSessionDao.createExportEvent(
                eventDate,
                uplan.getCHECK_ID() + ": UPLAN_UNREGULAR_294_INITIALIZATION",
                exportSession
        );
        log.info("{} : Created {}", requestNumber, exportEvent);

        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createUplanUnregular294Initialization(
                uuid,
                mailer,
                addressee,
                KO_NAME,
                uplan,
                records
        );
        final String result = JAXBMarshallerUtil.marshalAsString(requestNumber, requestMessage);
        log.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            checkErp.setNote("Error: after serialization empty message body");
            checkErp.setStatusErp(enumDao.get("ERP_CONVERSATION_STATUS", "ERROR"));
            em.merge(checkErp);
        }

        final CheckHistory history = checkErpDao.createHistory(checkErp, "MGI", eventDate, "UPLAN_UNREGULAR_294_INITIALIZATION", result, uuid);
        log.info("{} : Created {}", requestNumber, history);
        try {
            messageSender.send(result);
            return uuid;
        } catch (final Exception e) {
            log.error("{} : Error while sending JMS message", requestNumber, e);
            final String errorMessage = "Not sent to JMS: " + e.getMessage();
            checkErp.setNote(errorMessage);
            checkErp.setStatusErp(enumDao.get("ERP_CONVERSATION_STATUS", "ERROR"));
            em.merge(checkErp);
            return errorMessage;
        }
    }

    public String sendPlanRegular294Initialization(
            final long requestNumber,
            final String description,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Plan plan,
            final String acceptedName,
            final Integer year,
            final Set<PlanRecord> records
    ) {
        final String uuid = UUID.randomUUID().toString();
        log.info("#{} : assigned UUID='{}'", requestNumber, uuid);
//          final JAXBElement<RequestMsg> requestMessage = MessageFactory.createPlanRegular294Initialization(
//                uuid,
//                mailer,
//                addressee,
//                KO_NAME,
//                StringUtils.defaultString(acceptedName, plan.getAcceptedName()),
//                year != null ? year : plan.getYear(),
//                records
//        );
//        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, uuid);
//        log.debug("{} : MESSAGE BODY:\n {}", uuid, result);
//        if (StringUtils.isEmpty(result)) {
//            return null;
//        }
//        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
//        final Tuple<PlanErp, Set<PlanRecErp>> tupleErp = planDao.createErp(plan, null, records, null, null, dataKind, tupleExpSession.left);
//        logPlanTuples(uuid, tupleExpSession, tupleErp, log);
//        try {
//            messageSender.send(result);
//            expSessionDao.setSessionStatus(tupleExpSession.left, SessionStatus.DONE);
//            return result;
//        } catch (final Exception e) {
//            log.error("{} : Error while sending JMS message", uuid, e);
//            final String errorMessage = "Not sent to JMS: " + e.getMessage();
//            expSessionDao.setSessionInfo(tupleExpSession.left, SessionStatus.ERROR, errorMessage);
//            planDao.setStatus(StatusErp.ERROR, tupleErp.left, tupleErp.right, errorMessage);
//            return null;
//        }
        return null;
    }


    /*














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
        log.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
        final Tuple<PlanErp, Set<PlanRecErp>> tupleErp = planDao.createErp(plan, erpID, records, erpIDMap, null, dataKind, tupleExpSession.left);
        logPlanTuples(uuid, tupleExpSession, tupleErp, log);
        try {
            messageSender.send(result);
            expSessionDao.setSessionStatus(tupleExpSession.left, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            log.error("{} : Error while sending JMS message", uuid, e);
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
        log.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
        final Tuple<UplanErp, Set<UplanRecErp>> tupleErp = uplanDao.createErp(uplan, id, records, erpIDMap, null, dataKind, tupleExpSession.left);
        logPlanTuples(uuid, tupleExpSession, tupleErp, log);
        try {
            messageSender.send(result);
            expSessionDao.setSessionStatus(tupleExpSession.left, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            log.error("{} : Error while sending JMS message", uuid, e);
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
        log.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
        final Tuple<PlanErp, Set<PlanRecErp>> tupleErp = planDao.createErp(plan, erpID, actMap, erpIDMap, null, dataKind, tupleExpSession.left);
        logPlanTuples(uuid, tupleExpSession, tupleErp, log);
        try {
            messageSender.send(result);
            expSessionDao.setSessionStatus(tupleExpSession.left, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            log.error("{} : Error while sending JMS message", uuid, e);
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
        log.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
        logTuple(uuid, tupleExpSession, log);
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
        log.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
        final Tuple<PlanErp, Set<PlanRecErp>> tupleErp = planDao.createErp(plan, erpID, actMap, erpIDMap, null, dataKind, tupleExpSession.left);
        logPlanTuples(uuid, tupleExpSession, tupleErp, log);
        try {
            messageSender.send(result);
            expSessionDao.setSessionStatus(tupleExpSession.left, SessionStatus.DONE);
            return result;
        } catch (final Exception e) {
            log.error("{} : Error while sending JMS message", uuid, e);
            final String errorMessage = "Not sent to JMS: " + e.getMessage();
            expSessionDao.setSessionInfo(tupleExpSession.left, SessionStatus.ERROR, errorMessage);
            planDao.setStatus(StatusErp.ERROR, tupleErp.left, tupleErp.right, errorMessage);
            return null;
        }
    }

     */

    private <A, B> void logPlanTuples(
            final String uuid, final Tuple<ExpSession, ExpSessionEvent> tupleExpSession, final Tuple<A, Set<B>> tupleErp, final Logger logger
    ) {
        if (logger.isDebugEnabled()) {
            logger.debug("{} : Created ExportSession: {}", uuid, tupleExpSession.left);
            logger.debug("{} : Created ExportEvent: {}", uuid, tupleExpSession.right);
            logger.debug("{} : Created[main plan]: {}", uuid, tupleErp.left);
            for (Object x : tupleErp.right) {
                logger.debug("{} : Created[record]: {}", uuid, x);
            }
        }
    }

    private <A, B> void logTuple(final long requestNumber, final Tuple<A, B> tuple) {
        log.debug("#{} : Created[L]: {}", requestNumber, tuple.left);
        log.debug("#{} : Created[R]: {}", requestNumber, tuple.right);
    }


}
