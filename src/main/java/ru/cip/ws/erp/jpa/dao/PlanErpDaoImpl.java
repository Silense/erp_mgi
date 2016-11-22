package ru.cip.ws.erp.jpa.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jpa.entity.PlanErp;
import ru.cip.ws.erp.jpa.entity.enums.StatusErp;
import ru.cip.ws.erp.jpa.entity.sessions.ExpSession;
import ru.cip.ws.erp.jpa.entity.views.Plan;
import ru.cip.ws.erp.servlet.DataKindEnum;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

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
            final Plan plan, final Integer prosecutorId, final DataKindEnum dataKind, final ExpSession expSession, final BigInteger erpID
    ) {
        return createPlanErp(plan.getId(), prosecutorId, dataKind, expSession, erpID);
    }

    public PlanErp createPlanErp(final Plan plan, final Integer prosecutorId, final DataKindEnum dataKind, final ExpSession expSession) {
        return createPlanErp(plan.getId(), prosecutorId, dataKind, expSession, null);
    }

    public PlanErp createPlanErp(
            final Integer cipPlanID, final Integer prosecutorId, final DataKindEnum dataKind, final ExpSession expSession, final BigInteger erpID
    ) {
        final PlanErp result = new PlanErp();
        result.setProsecutor(prosecutorId);
        if (erpID != null) {
            result.setErpId(erpID);
        } else {
            result.setErpId(null);
        }
        result.setDataKind(dataKind);
        result.setStatus(StatusErp.WAIT);
        result.setPlanId(cipPlanID);
        result.setExpSession(expSession);
        em.persist(result);
        return result;
    }

    public void setStatus(final PlanErp planErp, final StatusErp status) {
        planErp.setStatus(status);
        em.merge(planErp);
    }

    public PlanErp getById(final Integer id) {
        return em.find(PlanErp.class, id);
    }

    public void setExportSessionAndStatus(final PlanErp planErp, final StatusErp status, final ExpSession exportSession) {
        planErp.setExpSession(exportSession);
        setStatus(planErp, status);
    }

    public PlanErp getByRequestId(final String requestId) {
        if (StringUtils.isEmpty(requestId)) {
            return null;
        }
        final ExpSession exp_session = exportSessionDao.getSessionByEXT_PACKAGE_ID(requestId);
        return getByExportSession(exp_session);
    }

    private PlanErp getByExportSession(final ExpSession exp_session) {
        if (exp_session == null) {
            return null;
        }
        return getByExportSession(exp_session.getId());
    }

    private PlanErp getByExportSession(final Integer expSessionId) {
        final List<PlanErp> resultList = em.createQuery("SELECT a FROM PlanErp a WHERE a.expSession.id = :expSessionId", PlanErp.class).setParameter(
                "expSessionId", expSessionId
        ).getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }

    public void setIDFromErp(final PlanErp planErp, final BigInteger erpID, final StatusErp status, final String totalValid) {
        planErp.setErpId(erpID);
        planErp.setTotalValid(totalValid);
        setStatus(planErp, status);
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
        ).setParameter("planId", plan.getId()).setParameter("statuses", StatusErp.incorrectStatuses()).setParameter("dataKind", dataKind)
                .getResultList();
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
}
