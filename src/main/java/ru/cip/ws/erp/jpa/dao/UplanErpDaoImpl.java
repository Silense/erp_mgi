package ru.cip.ws.erp.jpa.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jpa.entity.PlanErp;
import ru.cip.ws.erp.jpa.entity.UplanErp;
import ru.cip.ws.erp.jpa.entity.UplanRecErp;
import ru.cip.ws.erp.jpa.entity.enums.StatusErp;
import ru.cip.ws.erp.jpa.entity.sessions.ExpSession;
import ru.cip.ws.erp.jpa.entity.views.Uplan;
import ru.cip.ws.erp.jpa.entity.views.UplanRecord;
import ru.cip.ws.erp.servlet.DataKindEnum;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: Upatov Egor <br>
 * Date: 22.11.2016, 7:27 <br>
 * Description:
 */
@Repository
@Transactional
public class UplanErpDaoImpl {
    private final static Logger logger = LoggerFactory.getLogger(UplanErpDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    public UplanErp getByUUID(final String uuid) {
        final List<UplanErp> resultList = em.createQuery(
                "SELECT a FROM UplanErp a " +
                        "INNER JOIN a.expSession s " +
                        "LEFT JOIN FETCH a.records r " +
                        "WHERE s.EXT_PACKAGE_ID = :uuid", UplanErp.class
        ).setParameter("uuid", uuid).getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }



    public Tuple<UplanErp, Set<UplanRecErp>> createErp(
            final Uplan uplan,
            final BigInteger erpID,
            final Set<UplanRecord> records,
            final Map<Long, BigInteger> erpIDMap,
            final Integer prosecutor,
            final DataKindEnum dataKind,
            final ExpSession exportSession
    ) {
        final UplanErp uplanErp = createUplanErp(uplan, prosecutor, dataKind, exportSession, erpID);
        final Set<UplanRecErp> recs = new HashSet<>(records.size());
        for (UplanRecord x : records) {
            recs.add(createUplanRecErp(uplanErp, x.getCORRELATION_ID(), erpIDMap != null ? erpIDMap.get(x.getCORRELATION_ID()) : null));
        }
        return new Tuple<>(uplanErp, recs);
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

    public UplanRecErp createUplanRecErp(final UplanErp uplanErp, final Long correlationId, final BigInteger erpId) {
        final UplanRecErp result = new UplanRecErp();
        result.setPlan(uplanErp);
        result.setErpId(erpId);
        result.setStatus(StatusErp.WAIT);
        result.setCorrelationId(correlationId);
        em.persist(result);
        return result;
    }

    public void setStatus(final StatusErp status, final UplanErp planErp, final Set<UplanRecErp> records, final String message) {
        setStatus(status, planErp, message);
        for (UplanRecErp x : records) {
            x.setStatus(status);
            x.setTotalValid(message);
            em.merge(x);
        }
    }


    public void setStatus(final StatusErp status, final UplanErp planErp, final String message) {
        planErp.setStatus(status);
        planErp.setTotalValid(message);
        em.merge(planErp);
    }


}
