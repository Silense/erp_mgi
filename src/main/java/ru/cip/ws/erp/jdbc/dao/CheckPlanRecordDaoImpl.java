package ru.cip.ws.erp.jdbc.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlanRecord;
import ru.cip.ws.erp.jdbc.entity.PlanCheckErp;
import ru.cip.ws.erp.jdbc.entity.PlanCheckRecErp;
import ru.cip.ws.erp.jdbc.entity.StatusErp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Upatov Egor <br>
 * Date: 11.09.2016, 16:03 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
@Transactional
public class CheckPlanRecordDaoImpl {

    @PersistenceContext
    private EntityManager em;

    public List<CipCheckPlanRecord> getAllRecordsFromView() {
        return em.createQuery("SELECT a FROM CipCheckPlanRecord a ", CipCheckPlanRecord.class).getResultList();
    }

    public List<CipCheckPlanRecord> getRecordsFromViewByPlanId(final int check_plan_id) {
        return em.createQuery("SELECT a FROM CipCheckPlanRecord a WHERE a.plan.id = :check_plan_id", CipCheckPlanRecord.class).setParameter(
                "check_plan_id",
                check_plan_id
        ).getResultList();
    }


    public PlanCheckRecErp createPlanCheckRecErp(final PlanCheckErp plan, final CipCheckPlanRecord record) {
        final PlanCheckRecErp result = new PlanCheckRecErp();
        result.setPlan(plan);
        result.setErpId(null);
        result.setStatus(StatusErp.WAIT);
        result.setCorrelationId(record.getCorrelationId());
        em.persist(result);
        return result;
    }

    public void setStatus(final List<PlanCheckRecErp> records, final StatusErp status) {
        for (PlanCheckRecErp record : records) {
            setStatus(record, status);
        }
    }

    public void setStatus(final PlanCheckRecErp record, final StatusErp status) {
        record.setStatus(status);
        em.merge(record);
    }

    public List<PlanCheckRecErp> getRecordsByPlan(final Integer planId) {
        return em.createQuery("SELECT a FROM PlanCheckRecErp a WHERE a.plan.id = :plan_id", PlanCheckRecErp.class)
                .setParameter("plan_id", planId).getResultList();
    }

    public List<PlanCheckRecErp> getRecordsByPlan(final PlanCheckErp planCheckErp) {
        if (null != planCheckErp) {
            return getRecordsByPlan(planCheckErp.getId());
        }
        return new ArrayList<>(0);
    }

    public void setIDFromErp(final PlanCheckRecErp record, final BigInteger id, final StatusErp status, final String totalValid) {
        record.setErpId(id);
        record.setTotalValid(totalValid);
        setStatus(record, status);
    }
}
