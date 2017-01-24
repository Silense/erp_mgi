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
import ru.cip.ws.erp.jpa.entity.views.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBElement;
import java.math.BigInteger;
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
        final String messageType = "PROSECUTOR_ASK";
        final String uuid = UUID.randomUUID().toString();
        log.info("#{} : assigned UUID='{}'", requestNumber, uuid);
        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createSessionInfo(description, messageType, 1);
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




    public String sendUplanUnregular294Initialization(
            final String logTag,
            final ExpSession session,
            final String uuid,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Uplan check,
            final Set<UplanRecord> records,
            final CheckErp checkErp
    ) {
        final String messageType = "UPLAN_UNREGULAR_294_INITIALIZATION";
        final String eventInfo = String.format("Размещение внеплановой проверки[%d] под номером '%s' c requestID='%s' и типом запроса %s", check.getCHECK_ID(), check.getORDER_NUM(), uuid, messageType);
        final ExpSessionEvent exportEvent = expSessionDao.createEvent(eventInfo, session);
        log.info("{} : Created {}", logTag, exportEvent);

        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createUplanUnregular294Initialization(
                uuid,
                mailer,
                addressee,
                KO_NAME,
                check,
                records
        );
        return sendJaxbMessage(logTag, uuid, checkErp, messageType, requestMessage);
    }

    public String sendUplanUnregular294Correction(
            final String requestNumber,
            final ExpSession session,
            final String uuid,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Uplan check,
            final CheckErp checkErp,
            final Set<UplanRecord> records,
            final Set<CheckRecordErp> checkRecords
    ) {
        final String messageType = "UPLAN_UNREGULAR_294_CORRECTION";
        final String eventInfo = String.format("Корректировка внеплановой проверки[%d] под номером '%s' c requestID='%s' и типом запроса %s", check.getCHECK_ID(), check.getORDER_NUM(), uuid, messageType);
        final ExpSessionEvent exportEvent = expSessionDao.createEvent(eventInfo, session);
        log.info("{} : Created {}", requestNumber, exportEvent);

        final Map<BigInteger, BigInteger> erpIDMap = new HashMap<>(records.size());
        for (UplanRecord record : records) {
            for (CheckRecordErp checkRecord : checkRecords) {
                if(Objects.equals(checkRecord.getCorrelationId(), record.getCORRELATION_ID())){
                    erpIDMap.put(record.getCORRELATION_ID(), checkRecord.getErpCode());
                    break;
                }
            }
        }

        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createUplanUnregular294Correction(
                uuid, mailer, addressee, KO_NAME, check, checkErp.getErpCode(), records, erpIDMap
        );
        return sendJaxbMessage(requestNumber, uuid, checkErp, messageType, requestMessage);
    }



    public String sendUplanResult294Initialization(
            final String requestNumber,
            final ExpSession session,
            final String uuid,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final int year,
            final Uplan check,
            final CheckErp checkErp,
            final Set<CheckRecordErp> records,
            final UplanAct act,
            final Set<UplanActViolation> violations
    ) {
        final String messageType = "UPLAN_RESULT_294_INITIALIZATION";
        final String eventInfo = String.format("Размещение результатов внеплановой проверки[%d] под номером '%s' c requestID='%s' и типом запроса %s", check.getCHECK_ID(), check.getORDER_NUM(), uuid, messageType);
        final ExpSessionEvent exportEvent = expSessionDao.createEvent(eventInfo, session);
        log.info("{} : Created {}", requestNumber, exportEvent);
        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createUplanResult294Initialization(
                uuid, mailer, addressee, year, records, act, violations, checkErp.getErpCode()
        );
        return sendJaxbMessage(requestNumber, uuid, checkErp, messageType, requestMessage);
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
    */

   

//    public String sendPlanResult294Correction(
//            final String uuid,
//            final String description,
//            final MessageToERPModelType.Mailer mailer,
//            final MessageToERPModelType.Addressee addressee,
//            final Plan plan,
//            final BigInteger erpID,
//            final int year,
//            final Map<PlanAct, Set<PlanActViolation>> actMap,
//            final Map<Long, BigInteger> erpIDMap
//    ) {
//        final DataKindEnum dataKind = DataKindEnum.PLAN_RESULT_294_CORRECTION;
//        final JAXBElement<RequestMsg> requestMessage = MessageFactory.createPlanResult294Correction(
//                uuid, mailer, addressee, year, actMap, erpID, erpIDMap
//        );
//        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, uuid);
//        log.debug("{} : MESSAGE BODY:\n {}", uuid, result);
//        if (StringUtils.isEmpty(result)) {
//            return null;
//        }
//        final Tuple<ExpSession, ExpSessionEvent> tupleExpSession = expSessionDao.createExportSessionInfo(uuid, description, dataKind.getCode());
//        final Tuple<PlanErp, Set<PlanRecErp>> tupleErp = planDao.createErp(plan, erpID, actMap, erpIDMap, null, dataKind, tupleExpSession.left);
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
//    }

    private String sendJaxbMessage(String requestNumber, String uuid, CheckErp checkErp, String messageType, JAXBElement<RequestMsg> requestMessage) {
        final String result = JAXBMarshallerUtil.marshalAsString(requestNumber, requestMessage);
        log.debug("{} : MESSAGE BODY:\n {}", uuid, result);
        if (StringUtils.isEmpty(result)) {
            checkErp.setNote("Error: after serialization empty message body");
            checkErp.setStatusErp(enumDao.get("ERP_CONVERSATION_STATUS", "ERROR"));
            em.merge(checkErp);
        }
        final CheckHistory history = checkErpDao.createHistory(checkErp, "MGI", messageType, result, uuid);
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


}
