package ru.cip.ws.erp.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.dto.AllocateUnregularParameter;
import ru.cip.ws.erp.dto.AllocateUnregularResultParameter;
import ru.cip.ws.erp.generated.erptypes.MessageToERPModelType;
import ru.cip.ws.erp.jpa.dao.CheckErpDaoImpl;
import ru.cip.ws.erp.jpa.dao.ExportSessionDaoImpl;
import ru.cip.ws.erp.jpa.dao.Tuple;
import ru.cip.ws.erp.jpa.entity.CheckErp;
import ru.cip.ws.erp.jpa.entity.CheckRecordErp;
import ru.cip.ws.erp.jpa.entity.enums.SessionStatus;
import ru.cip.ws.erp.jpa.entity.sessions.ExpSession;
import ru.cip.ws.erp.jpa.entity.views.UplanRecord;

import java.util.*;

/**
 * Author: Upatov Egor <br>
 * Date: 20.12.2016, 1:40 <br>
 * Description: Сервис размещения объектов в ЕТП\ЕРП
 */
@Repository
public class AllocationService {

    private final static Logger log = LoggerFactory.getLogger(AllocationService.class);

    @Autowired
    private ExportSessionDaoImpl expSessionDao;

    @Autowired
    private CheckErpDaoImpl checkErpDao;

    @Autowired
    private MessageService messageService;

    /**
     * Отправить в ЕРП запрос на получение списка прокуратур
     *
     * @param requestNumber префикс для логгирования
     * @param description   описание типа запроса
     * @return Результат размещения
     * где результат размещения- номер requestId для ЕРП в случае успешной отправки
     * или текстовое пояснение почему проверка не была размещена
     */
    public String processProsecutorAck(long requestNumber, String description) {
        return messageService.sendProsecutorAck(requestNumber, description);
    }

    /**
     * Массовое размещение Внеплановых проверок в ЕРП
     *
     * @param logTag      префикс-номер для логиирования
     * @param desc Общее пояснение к размещению проверок (для EXP_SESSION)
     * @param mailer      от имени какой структуры будем размещать
     * @param addressee   на имя какой структуры будем размещать
     * @param KO_NAME
     * @param parameters  Списко структур для отправки в ЕРП
     * @return карта результатов размещения <Идентификатор проверки, Результат размещения>,
     * где результат размещения- номер requestId для ЕРП в случае успешной отправки
     * или текстовое пояснение почему проверка не была размещена
     */
    public Map<String, String> allocateUnregularBatch(
            final long logTag,
            final String desc,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Set<AllocateUnregularParameter> parameters
    ) {
        final String actionName = "UNREGULAR_ALLOCATION_BATCH";
        log.info("#{} : Start {} [{} checks]", logTag, actionName, parameters.size());
        if (parameters.isEmpty()) {
            log.warn("#{} : End {}. No checks passed.", logTag, actionName);
            return Collections.emptyMap();
        }
        final ExpSession session = expSessionDao.createSession(desc, actionName, parameters.size());
        log.info("#{} : Created {}", logTag, session);
        final Map<String, String> result = new LinkedHashMap<>(parameters.size());
        for (AllocateUnregularParameter param : parameters) {
            result.put(param.getCheckId(), allocateUnregular(logTag, mailer, addressee, KO_NAME, session, param));
        }
        expSessionDao.setSessionStatus(session, SessionStatus.DONE);
        log.info("#{} : End {}. Result: {}", logTag, actionName, result);
        return result;
    }

    /**
     * Размещение одиночной Внеплановой проверки в ЕРП
     *
     * @param requestNumber префикс для логгирования
     * @param mailer        от имени какой структуры будем размещать
     * @param addressee     на имя какой структуры будем размещать
     * @param KO_NAME
     * @param session       сессия экспорта в рамках которой происходит размещение
     * @param parameter     Структура с данными для размещения внеплановой проверки
     * @return
     */
    public String allocateUnregular(
            final long requestNumber,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final ExpSession session,
            final AllocateUnregularParameter parameter
    ) {
        final String logTag = "#" + requestNumber + "-" + parameter.getCheckId();
        final String actionName = "UNREGULAR_ALLOCATION";
        log.info("{} : start {}", logTag, actionName);
        Tuple<CheckErp, Set<CheckRecordErp>> checkErpTuple = checkErpDao.getCheckErp(parameter.getCheck().getCHECK_ID(), "UNREGULAR");
        boolean needInitialization = true;
        final String uuid = UUID.randomUUID().toString();
        if (checkErpTuple == null) {
            log.debug("{} : UUID assign [{}]. Not allocated before", logTag, uuid);
            checkErpTuple = checkErpDao.createErp(parameter.getCheck(), parameter.getRecords(), uuid);
            log.debug("{} : Created {}", logTag, checkErpTuple.left);
            for (CheckRecordErp recordErp : checkErpTuple.right) {
                log.debug("{} : Created {}", logTag, recordErp);
            }
            needInitialization = true;
        } else {
            final String stateCode = checkErpTuple.left.getState().getCode();
            if ("ERROR_ALLOCATION".equalsIgnoreCase(stateCode)) {
                log.debug("{} : UUID assign [{}]. ERROR_ALLOCATION", logTag, uuid);
                syncRecords(logTag,checkErpTuple.left, checkErpTuple.right, parameter.getRecords());
                checkErpDao.assignUUID(checkErpTuple.left, uuid);
                needInitialization = true;
            } else if ("PARTIAL_ALLOCATION".equalsIgnoreCase(stateCode)) {
                log.debug("{} : UUID assign [{}]. PARTIAL_ALLOCATION", logTag, uuid);
                syncRecords(logTag, checkErpTuple.left, checkErpTuple.right, parameter.getRecords());
                checkErpDao.assignUUID(checkErpTuple.left, uuid);
                needInitialization = false;
            } else {
                final String result = "Check is not in available for allocation state [" + stateCode + "]";
                log.warn("{} : end {}. Skipped cause '{}'", logTag, actionName, result);
                return result;
            }
        }
        final String result;
        if (needInitialization) {
            result = messageService.sendUplanUnregular294Initialization(
                    logTag,
                    session,
                    uuid,
                    mailer,
                    addressee,
                    KO_NAME,
                    parameter.getCheck(),
                    parameter.getRecords(),
                    checkErpTuple.left
            );
        } else {
            result = messageService.sendUplanUnregular294Correction(
                    logTag,
                    session,
                    uuid,
                    mailer,
                    addressee,
                    KO_NAME,
                    parameter.getCheck(),
                    checkErpTuple.left,
                    parameter.getRecords(),
                    checkErpTuple.right
            );
        }
        log.info("{} : end. Result = '{}'", requestNumber, result);
        return result;
    }

    private void syncRecords(final String logTag, CheckErp checkErp, Set<CheckRecordErp> erpRecords, Set<UplanRecord> records) {
        for (UplanRecord record : records) {
            boolean found = false;
            for (CheckRecordErp erpRecord : erpRecords) {
                if(Objects.equals(erpRecord.getCorrelationId(), record.getCORRELATION_ID())){
                    found = true;
                    break;
                }
            }
            if(!found){
                final CheckRecordErp erpRecord = checkErpDao.createCheckRecordErp(checkErp, record);
                log.error("{} : MISMATCH IN RECORDS. FOR {} CREATED {} ", logTag, record, erpRecord);
            }
        }
    }


    public Map<String, String> allocateUnregularResultBatch(
            final long logTag,
            final String desc,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Set<AllocateUnregularResultParameter> parameters
    ) {
        final String actionType = "UNREGULAR_RESULT_ALLOCATION_BATCH";
        log.info("#{} : Start {} [{} checks]", logTag, actionType, parameters.size());
        if (parameters.isEmpty()) {
            log.warn("#{} : End {}. No checks passed.", logTag, actionType);
            return Collections.emptyMap();
        }
        final ExpSession session = expSessionDao.createSession(desc, actionType, parameters.size());
        log.info("#{} : Created {}", logTag, session);
        final Map<String, String> result = new LinkedHashMap<>(parameters.size());
        for (AllocateUnregularResultParameter param : parameters) {
            result.put(param.getCheckId(), allocateUnregularResult(logTag, mailer, addressee, KO_NAME, session, param));
        }
        expSessionDao.setSessionStatus(session, SessionStatus.DONE);
        log.info("#{} : End {}. Result: {}", logTag, actionType, result);
        return result;
    }

    private String allocateUnregularResult(
            final long requestNumber,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String ko_name,
            final ExpSession session,
            final AllocateUnregularResultParameter parameter) {
        final String logTag = "#" + requestNumber + "-" + parameter.getCheckId();
        log.info("{} : start allocate UnregularCheckResult for CHECK[{}]", logTag, parameter.getCheckId());

       //TODO messageService.sendUplanUnre
        return null;

    }


}
