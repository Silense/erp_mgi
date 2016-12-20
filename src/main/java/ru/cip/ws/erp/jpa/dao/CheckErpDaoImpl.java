package ru.cip.ws.erp.jpa.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.business.LocalFileStorage;
import ru.cip.ws.erp.jpa.entity.CheckErp;
import ru.cip.ws.erp.jpa.entity.CheckHistory;
import ru.cip.ws.erp.jpa.entity.CheckRecordErp;
import ru.cip.ws.erp.jpa.entity.RsysEnum;
import ru.cip.ws.erp.jpa.entity.views.Uplan;
import ru.cip.ws.erp.jpa.entity.views.UplanRecord;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Upatov Egor <br>
 * Date: 16.09.2016, 2:35 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */

@Repository
@Transactional
public class CheckErpDaoImpl {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EnumDaoImpl enumDao;

    @Autowired
    private LocalFileStorage fileStorage;

    public Tuple<CheckErp, Set<CheckRecordErp>> getCheckErp(final BigInteger checkId, final String erpCheckTypeCode) {
        final List<CheckErp> resultList = em.createQuery(
                "SELECT a " +
                        "FROM CheckErp a " +
                        "JOIN FETCH a.records r " +
                        "WHERE a.checkId = :checkId " +
                        "AND a.checkType.code = :erpCheckTypeCode ", CheckErp.class
        )
                .setParameter("checkId", checkId)
                .setParameter("erpCheckTypeCode", erpCheckTypeCode)
                .getResultList();
        if(!resultList.isEmpty()){
            CheckErp item = resultList.get(0);
            return new Tuple<>(item, item.getRecords());
        } else {
            return null;
        }
    }

    public Tuple<CheckErp, Set<CheckRecordErp>> getByCorrelationUUID(final String uuid) {
        final List<CheckErp> resultList = em.createQuery(
                "SELECT a " +
                        "FROM CheckErp a " +
                        "JOIN FETCH a.records r " +
                        "WHERE a.correlationUUID = :uuid ", CheckErp.class
        )
                .setParameter("uuid", uuid)
                .getResultList();
        if(!resultList.isEmpty()){
            CheckErp item = resultList.get(0);
            return new Tuple<>(item, item.getRecords());
        } else {
            return null;
        }
    }

    public CheckErp createCheckErp(final Uplan uplan, final String correlationUUID) {
        final CheckErp result = new CheckErp();
        result.setCheckId(uplan.getCHECK_ID());
        result.setCheckType(enumDao.get("ERP_CHECK_TYPE", "UNREGULAR"));
        result.setProsecutor(null);
        result.setState(enumDao.get("ERP_CHECK_STATE", "WAIT_ALLOCATION"));
        result.setStatusErp(enumDao.get("ERP_CONVERSATION_STATUS", "WAIT"));
        result.setCorrelationUUID(correlationUUID);
        result.setErpCode(null);
        result.setNote(null);
        result.setAttempts(1);
        result.setLastErpDate(new Date());
        em.persist(result);
        return result;
    }


    public Tuple<CheckErp, Set<CheckRecordErp>> createErp(final Uplan uplan, final Set<UplanRecord> records, final String correlationUUID){
        final CheckErp checkErp = createCheckErp(uplan, correlationUUID);
        final Set<CheckRecordErp> checkRecords = new LinkedHashSet<>(records.size());
        for (UplanRecord record : records) {
           checkRecords.add(createCheckRecordErp(checkErp, record));
        }
        return new Tuple<>(checkErp, checkRecords);
    }

    public CheckRecordErp createCheckRecordErp(final CheckErp checkErp, final UplanRecord record) {
        final CheckRecordErp result = new CheckRecordErp();
        result.setCheck(checkErp);
        result.setCorrelationId(record.getCORRELATION_ID());
        result.setErpCode(null);
        result.setNote(null);
        em.persist(result);
        return result;
    }


    public CheckHistory createHistory(
            final CheckErp checkErp,
            final String messageSource,
            final String note,
            final String messageContent,
            final String correlationUUID) {
        final CheckHistory result = new CheckHistory();
        result.setCheck(checkErp);
        result.setMessageSource(enumDao.get("ERP_MESSAGE_SOURCE", messageSource));
        result.setEventDatetime(new Date());
        result.setNote(note);
        result.setRawMessage(fileStorage.createFile(checkErp.getCheckId(), correlationUUID, messageContent));
        result.setCorrelationUUID(correlationUUID);
        em.persist(result);
        return result;
    }


    public void setErpStatus(final CheckErp checkErp, final RsysEnum erpStatus, final String note, final Date erpDate) {
        checkErp.setStatusErp(erpStatus);
        checkErp.setNote(note);
        checkErp.setLastErpDate(erpDate);
        em.merge(checkErp);
    }

    public void setErpCode(
            final CheckErp checkErp,
            final RsysEnum erpStatus,
            final String note,
            final Date responseDate,
            final BigInteger erpID
    ) {
        checkErp.setErpCode(erpID);
        setErpStatus(checkErp, erpStatus, note, responseDate);
    }

    public void setRecordErpCode(final CheckRecordErp recordErp, final BigInteger erpID, final String note) {
        recordErp.setErpCode(erpID);
        recordErp.setNote(note);
        em.merge(recordErp);
    }

    public void setErpCodeAndState(
            final CheckErp checkErp, final RsysEnum erpStatus, final String note, final Date responseDate, final BigInteger erpId, final RsysEnum state
    ) {
        checkErp.setState(state);
        setErpCode(checkErp, erpStatus, note, responseDate, erpId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void assignUUID(final CheckErp checkErp, final String uuid) {
        checkErp.setAttempts(checkErp.getAttempts() + 1);
        checkErp.setState(enumDao.get("ERP_CHECK_STATE", "WAIT_ALLOCATION"));
        checkErp.setStatusErp(enumDao.get("ERP_CONVERSATION_STATUS", "WAIT"));
        checkErp.setCorrelationUUID(uuid);
        em.merge(checkErp);
    }

    public void setState(final CheckErp checkErp, final RsysEnum state,final Date responseDate) {
        checkErp.setState(state);
        checkErp.setLastErpDate(responseDate);
        em.merge(checkErp);
    }
}
