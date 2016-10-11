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
 * Date: 08.10.2016, 17:32 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */

@Repository
@Transactional
public class PlanCheckRecordErpDaoImpl {

    @PersistenceContext
    private EntityManager em;

    public PlanCheckRecErp createPlanCheckRecErp(final PlanCheckErp plan, final Integer correlationId, final BigInteger erpID) {
        final PlanCheckRecErp result = new PlanCheckRecErp();
        result.setPlan(plan);
        if(erpID != null) {
            result.setErpId(erpID);
        } else {
            result.setErpId(null);
        }
        result.setStatus(StatusErp.WAIT);
        result.setCorrelationId(correlationId);
        em.persist(result);
        return result;
    }

    public PlanCheckRecErp createPlanCheckRecErp(final PlanCheckErp plan, final CipCheckPlanRecord record, final BigInteger erpID) {
      return createPlanCheckRecErp(plan, record.getCorrelationId(), erpID);
    }


    public PlanCheckRecErp createPlanCheckRecErp(final PlanCheckErp plan, final CipCheckPlanRecord record) {
         return createPlanCheckRecErp(plan, record, null);
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
