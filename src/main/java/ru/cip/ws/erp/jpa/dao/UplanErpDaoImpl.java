package ru.cip.ws.erp.jpa.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jpa.entity.UplanErp;
import ru.cip.ws.erp.jpa.entity.UplanRecErp;
import ru.cip.ws.erp.jpa.entity.enums.SessionStatus;
import ru.cip.ws.erp.jpa.entity.enums.StatusErp;
import ru.cip.ws.erp.jpa.entity.sessions.ExpSession;
import ru.cip.ws.erp.jpa.entity.views.Uplan;
import ru.cip.ws.erp.jpa.entity.views.UplanRecord;
import ru.cip.ws.erp.servlet.DataKindEnum;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: Upatov Egor <br>
 * Date: 22.11.2016, 7:27 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
@Transactional
public class UplanErpDaoImpl {
    private final static Logger logger = LoggerFactory.getLogger(UplanErpDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ExportSessionDaoImpl exportSessionDao;

    public Tuple<UplanErp, List<UplanRecErp>> createErp(
            final String requestId,
            final Uplan uplan,
            final BigInteger uplanId,
            final List<UplanRecord> uplanRecords,
            final Map<Long, BigInteger> erpIDByCorrelatedID,
            final Integer prosecutor,
            final DataKindEnum dataKind,
            final ExpSession exportSession
    ) {
        final UplanErp uplanErp = createUplanErp(uplan, prosecutor, dataKind, exportSession, uplanId);
        logger.info("{} : Created: {}", requestId, uplanErp);
        final List<UplanRecErp> uplanRecErpList = new ArrayList<>(uplanRecords.size());
        for (UplanRecord record : uplanRecords) {
            final UplanRecErp uplanRecErp;
            if(erpIDByCorrelatedID != null && erpIDByCorrelatedID.containsKey(record.getCORRELATION_ID())) {
                uplanRecErp = createUplanRecErp(uplanErp, record, erpIDByCorrelatedID.get(record.getCORRELATION_ID()));
            } else {
                uplanRecErp = createUplanRecErp(uplanErp, record);
            }
            uplanRecErpList.add(uplanRecErp);
            logger.info("{} : Created: {}", requestId, uplanRecErp);
        }
        return new Tuple<>(uplanErp, uplanRecErpList);
    }

    public Tuple<UplanErp, List<UplanRecErp>> createErp(
            final String requestId,
            final Uplan uplan,
            final List<UplanRecord> uplanRecords,
            final Integer prosecutor,
            final DataKindEnum dataKind,
            final ExpSession exportSession
    ) {
       return createErp(requestId, uplan, null, uplanRecords, null, prosecutor, dataKind, exportSession);
    }

    public UplanErp createUplanErp(final Uplan uplan, final DataKindEnum dataKind, final ExpSession exportSession) {
        return createUplanErp(uplan, null, dataKind, exportSession);
    }

    public UplanErp createUplanErp(final Uplan uplan, final Integer prosecutor, final DataKindEnum dataKind, final ExpSession exportSession) {
        return createUplanErp(uplan, prosecutor, dataKind, exportSession, null);
    }

    public UplanErp createUplanErp(
            final Uplan uplan,
            final Integer prosecutor,
            final DataKindEnum dataKind,
            final ExpSession exportSession,
            final BigInteger erpId
    ) {
        final UplanErp result = new UplanErp();
        result.setProsecutor(prosecutor);
        result.setErpId(erpId);
        result.setDataKind(dataKind);
        result.setStatus(StatusErp.WAIT);
        result.setPlanId(uplan.getCHECK_ID().intValue());
        result.setExpSession(exportSession);
        em.persist(result);
        return result;
    }


    public UplanRecErp createUplanRecErp(final UplanErp uplanErp, final UplanRecord record) {
        return createUplanRecErp(uplanErp, record, null);
    }

    public UplanRecErp createUplanRecErp(final UplanErp uplanErp, final UplanRecord record, final BigInteger erpId) {
        final UplanRecErp result = new UplanRecErp();
        result.setUplanErp(uplanErp);
        result.setErpId(erpId);
        result.setStatus(StatusErp.WAIT);
        result.setCorrelationId(record.getCORRELATION_ID());
        em.persist(result);
        return result;
    }

    public void setErrorStatus(final Tuple<UplanErp, List<UplanRecErp>> tuple, final String message) {
        final UplanErp uplanErp = tuple.left;
        uplanErp.setStatus(StatusErp.ERROR);
        uplanErp.setTotalValid(message);
        em.merge(uplanErp);
        for (UplanRecErp recErp : tuple.right) {
            recErp.setStatus(StatusErp.ERROR);
            recErp.setTotalValid(message);
            em.merge(recErp);
        }
        exportSessionDao.setSessionInfo(uplanErp.getExpSession(), SessionStatus.ERROR, message);
    }


}
