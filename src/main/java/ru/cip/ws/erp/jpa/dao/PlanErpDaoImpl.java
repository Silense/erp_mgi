package ru.cip.ws.erp.jpa.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jpa.entity.PlanErp;
import ru.cip.ws.erp.jpa.entity.PlanRecErp;
import ru.cip.ws.erp.jpa.entity.enums.StatusErp;
import ru.cip.ws.erp.jpa.entity.sessions.ExpSession;
import ru.cip.ws.erp.jpa.entity.views.Plan;
import ru.cip.ws.erp.jpa.entity.views.PlanAct;
import ru.cip.ws.erp.jpa.entity.views.PlanActViolation;
import ru.cip.ws.erp.jpa.entity.views.PlanRecord;
import ru.cip.ws.erp.servlet.DataKindEnum;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: Upatov Egor <br>
 * Date: 16.09.2016, 2:35 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */

@Repository
@Transactional
public class PlanErpDaoImpl {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ExportSessionDaoImpl exportSessionDao;

    public PlanErp createPlanErp(
            final Integer planId, final Integer prosecutorId, final DataKindEnum dataKind, final ExpSession expSession, final BigInteger erpID
    ) {
        final PlanErp result = new PlanErp();
        result.setProsecutor(prosecutorId);
        result.setErpId(erpID);
        result.setDataKind(dataKind);
        result.setStatus(StatusErp.WAIT);
        result.setPlanId(planId);
        result.setExpSession(expSession);
        em.persist(result);
        return result;
    }

    public PlanRecErp createPlanRecErp(final PlanErp plan, final Long correlationId, final BigInteger erpID) {
        final PlanRecErp result = new PlanRecErp();
        result.setPlan(plan);
        result.setErpId(erpID);
        result.setStatus(StatusErp.WAIT);
        result.setCorrelationId(correlationId);
        em.persist(result);
        return result;
    }


    public PlanErp getByUUID(final String uuid) {
        final List<PlanErp> resultList = em.createQuery(
                "SELECT a FROM PlanErp a " +
                        "INNER JOIN a.expSession s " +
                        "LEFT JOIN FETCH a.records r " +
                        "WHERE s.EXT_PACKAGE_ID = :uuid", PlanErp.class
        ).setParameter("uuid", uuid).getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }


    public boolean hasActivePlan(final Plan plan) {
        final List<PlanErp> resultList = getActiveByPlan(plan);
        return resultList != null && !resultList.isEmpty();
    }

    public List<PlanErp> getActiveByPlan(final Plan plan) {
        return em.createQuery(
                "SELECT a FROM PlanErp a WHERE a.planId = :planId AND a.status NOT IN :statuses ORDER BY a.expSession.START_DATE DESC", PlanErp.class
        ).setParameter("planId", plan.getId()).setParameter("statuses", StatusErp.incorrectStatuses()).getResultList();
    }

    public List<PlanErp> getActiveByPlan(final Plan plan, final DataKindEnum dataKind) {
        return em.createQuery(
                "SELECT a FROM PlanErp a WHERE a.planId = :planId AND a.dataKind = :dataKind AND a.status NOT IN :statuses ORDER BY a.expSession.START_DATE DESC",
                PlanErp.class
        ).setParameter("planId", plan.getId()).setParameter("statuses", StatusErp.incorrectStatuses()).setParameter(
                "dataKind", dataKind
        ).getResultList();
    }

    public void cancel(final PlanErp planErp) {
        planErp.setStatus(StatusErp.CANCELED);
        em.merge(planErp);
    }

    public PlanErp getLastActiveByPlan(final Plan plan) {
        final List<PlanErp> resultList = getActiveByPlan(plan);
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }

    public PlanErp getLastFaultByPlan(final Plan plan) {
        final List<PlanErp> resultList = em.createQuery(
                "SELECT a FROM PlanErp a WHERE a.planId = :planId AND a.status = :status ORDER BY a.expSession.START_DATE DESC", PlanErp.class
        ).setParameter("planId", plan.getId()).setParameter("status", StatusErp.FAULT).getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }

    public PlanErp getLastActiveByPlanOrFault(final Plan plan) {
        final PlanErp result = getLastActiveByPlan(plan);
        return result != null ? result : getLastFaultByPlan(plan);
    }


    public Tuple<PlanErp, Set<PlanRecErp>> createErp(
            final Plan plan,
            final BigInteger erpID,
            final Set<PlanRecord> records,
            final Map<Long, BigInteger> erpIDMap,
            final Integer prosecutor,
            final DataKindEnum dataKind,
            final ExpSession session
    ) {
        final PlanErp planErp = createPlanErp(plan.getId(), prosecutor, dataKind, session, erpID);
        final Set<PlanRecErp> recs = new LinkedHashSet<>(records.size());
        for (PlanRecord x : records) {
            recs.add(createPlanRecErp(planErp, x.getCORRELATION_ID(), erpIDMap != null ? erpIDMap.get(x.getCORRELATION_ID()) : null));
        }
        return new Tuple<>(planErp, recs);
    }

    public Tuple<PlanErp, Set<PlanRecErp>> createErp(
            final Plan plan,
            final BigInteger erpID,
            final Map<PlanAct, Set<PlanActViolation>> actMap,
            final Map<Long, BigInteger> erpIDMap,
            final Integer prosecutor,
            final DataKindEnum dataKind,
            final ExpSession session
    ) {
        final PlanErp planErp = createPlanErp(plan.getId(), prosecutor, dataKind, session, erpID);
        final Set<PlanRecErp> recs = new LinkedHashSet<>(actMap.size());
        for (PlanAct x : actMap.keySet()) {
            recs.add(createPlanRecErp(planErp, x.getCorrelationID(), erpIDMap != null ? erpIDMap.get(x.getCorrelationID()) : null));
        }
        return new Tuple<>(planErp, recs);
    }


    public void setStatus(final StatusErp status, final PlanErp planErp, final Set<PlanRecErp> records, final String message) {
        setStatus(status, planErp, message);
        for (PlanRecErp x : records) {
            x.setStatus(status);
            x.setTotalValid(message);
            em.merge(x);
        }
    }


    public void setStatus(final StatusErp status, final PlanErp planErp, final String message) {
        planErp.setStatus(status);
        planErp.setTotalValid(message);
        em.merge(planErp);
    }

    public void setErpId(final PlanErp planErp, final BigInteger erpId, final StatusErp status, final String totalValid) {
        planErp.setErpId(erpId);
        setStatus(status, planErp, totalValid);
    }
    public void setErpId(final PlanRecErp planRecErp, final BigInteger erpId, final StatusErp status, final String totalValid) {
        planRecErp.setErpId(erpId);
        planRecErp.setStatus(status);
        planRecErp.setTotalValid(totalValid);
        em.merge(planRecErp);
    }
}
