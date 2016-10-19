package ru.cip.ws.erp.jdbc.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlan;
import ru.cip.ws.erp.jdbc.entity.ExpSession;
import ru.cip.ws.erp.jdbc.entity.PlanCheckErp;
import ru.cip.ws.erp.jdbc.entity.StatusErp;
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
public class PlanCheckErpDaoImpl {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ExportSessionDaoImpl exportSessionDao;


    public PlanCheckErp createPlanCheckErp(
            final CipCheckPlan checkPlan,
            final Integer prosecutorId,
            final DataKindEnum dataKind,
            final ExpSession expSession,
            final BigInteger erpID
    ) {
        return createPlanCheckErp(checkPlan.getId(), prosecutorId, dataKind, expSession, erpID);
    }

    public PlanCheckErp createPlanCheckErp(final CipCheckPlan checkPlan, final Integer prosecutorId, final DataKindEnum dataKind, final ExpSession expSession) {
        return createPlanCheckErp(checkPlan.getId(), prosecutorId, dataKind, expSession, null);
    }

    public PlanCheckErp createPlanCheckErp(
            final Integer cipCheckPlanID, final Integer prosecutorId, final DataKindEnum dataKind, final ExpSession expSession, final BigInteger erpID
    ) {
        final PlanCheckErp result = new PlanCheckErp();
        result.setProsecutor(prosecutorId);
        if (erpID != null) {
            result.setErpId(erpID);
        } else {
            result.setErpId(null);
        }
        result.setDataKind(dataKind);
        result.setStatus(StatusErp.WAIT);
        result.setCipChPlLglApprvdId(cipCheckPlanID);
        result.setExpSession(expSession);
        em.persist(result);
        return result;
    }

    public void setStatus(final PlanCheckErp planCheckErp, final StatusErp status) {
        planCheckErp.setStatus(status);
        em.merge(planCheckErp);
    }

    public PlanCheckErp getById(final Integer id) {
        return em.find(PlanCheckErp.class, id);
    }

    public void setExportSessionAndStatus(final PlanCheckErp planCheckErp, final StatusErp status, final ExpSession exportSession) {
        planCheckErp.setExpSession(exportSession);
        setStatus(planCheckErp, status);
    }

    public PlanCheckErp getByRequestId(final String requestId) {
        if (StringUtils.isEmpty(requestId)) {
            return null;
        }
        final ExpSession exp_session = exportSessionDao.getSessionByEXT_PACKAGE_ID(requestId);
        return getByExportSession(exp_session);
    }

    private PlanCheckErp getByExportSession(final ExpSession exp_session) {
        if (exp_session == null) {
            return null;
        }
        return getByExportSession(exp_session.getId());
    }

    private PlanCheckErp getByExportSession(final Integer expSessionId) {
        final List<PlanCheckErp> resultList = em.createQuery("SELECT a FROM PlanCheckErp a WHERE a.expSession.id = :expSessionId", PlanCheckErp.class)
                .setParameter("expSessionId", expSessionId).getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }

    public void setIDFromErp(final PlanCheckErp planCheckErp, final BigInteger erpID, final StatusErp status, final String totalValid) {
        planCheckErp.setErpId(erpID);
        planCheckErp.setTotalValid(totalValid);
        setStatus(planCheckErp, status);
    }

    public boolean hasActivePlan(final CipCheckPlan plan) {
        final List<PlanCheckErp> resultList = getActiveByPlan(plan);
        return resultList != null && !resultList.isEmpty();
    }

    public List<PlanCheckErp> getActiveByPlan(final CipCheckPlan plan) {
        return em.createQuery(
                "SELECT a FROM PlanCheckErp a " +
                        "WHERE a.cipChPlLglApprvdId = :planId " +
                        "AND a.status NOT IN :statuses" +
                        " ORDER BY a.expSession.START_DATE DESC", PlanCheckErp.class
        ).setParameter("planId", plan.getId()).setParameter("statuses", StatusErp.incorrectStatuses()).getResultList();
    }

    public List<PlanCheckErp> getActiveByPlan(final CipCheckPlan plan, final DataKindEnum dataKind) {
        return em.createQuery(
                "SELECT a FROM PlanCheckErp a WHERE a.cipChPlLglApprvdId = :planId AND a.dataKind = :dataKind AND a.status NOT IN :statuses" + " ORDER BY a.expSession.START_DATE DESC",
                PlanCheckErp.class
        ).setParameter("planId", plan.getId()).setParameter("statuses", StatusErp.incorrectStatuses()).setParameter("dataKind", dataKind)
                .getResultList();
    }

    public void cancel(final PlanCheckErp planCheckErp) {
        planCheckErp.setStatus(StatusErp.CANCELED);
        em.merge(planCheckErp);
    }

    public PlanCheckErp getLastActiveByPlan(final CipCheckPlan plan) {
        final List<PlanCheckErp> resultList = getActiveByPlan(plan);
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }

    public PlanCheckErp getLastFaultByPlan(final CipCheckPlan plan) {
        final List<PlanCheckErp> resultList = em.createQuery(
                "SELECT a FROM PlanCheckErp a " +
                        "WHERE a.cipChPlLglApprvdId = :planId " +
                        "AND a.status = :status" +
                        " ORDER BY a.expSession.START_DATE DESC", PlanCheckErp.class
        ).setParameter("planId", plan.getId()).setParameter("status", StatusErp.FAULT).getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }

    public PlanCheckErp getLastActiveByPlanOrFault(final CipCheckPlan plan) {
        final PlanCheckErp result = getLastActiveByPlan(plan);
        return result != null ? result : getLastFaultByPlan(plan);
    }
}
