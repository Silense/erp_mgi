package ru.cip.ws.erp.jdbc.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jdbc.entity.views.PlanRecord;
import ru.cip.ws.erp.jdbc.entity.PlanErp;
import ru.cip.ws.erp.jdbc.entity.PlanRecErp;
import ru.cip.ws.erp.jdbc.entity.enums.StatusErp;

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
public class PlanRecordErpDaoImpl {

    @PersistenceContext
    private EntityManager em;

    public PlanRecErp createPlanRecErp(final PlanErp plan, final Integer correlationId, final BigInteger erpID) {
        final PlanRecErp result = new PlanRecErp();
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

    public PlanRecErp createPlanRecErp(final PlanErp plan, final PlanRecord record, final BigInteger erpID) {
      return createPlanRecErp(plan, record.getCorrelationId(), erpID);
    }


    public PlanRecErp createPlanRecErp(final PlanErp plan, final PlanRecord record) {
         return createPlanRecErp(plan, record, null);
    }

    public void setStatus(final List<PlanRecErp> records, final StatusErp status) {
        for (PlanRecErp record : records) {
            setStatus(record, status);
        }
    }

    public void setStatus(final PlanRecErp record, final StatusErp status) {
        record.setStatus(status);
        em.merge(record);
    }

    public List<PlanRecErp> getRecordsByPlan(final Integer planId) {
        return em.createQuery("SELECT a FROM PlanRecErp a WHERE a.plan.id = :plan_id", PlanRecErp.class)
                .setParameter("plan_id", planId).getResultList();
    }

    public List<PlanRecErp> getRecordsByPlan(final PlanErp planErp) {
        if (null != planErp) {
            return getRecordsByPlan(planErp.getId());
        }
        return new ArrayList<>(0);
    }

    public void setIDFromErp(final PlanRecErp record, final BigInteger id, final StatusErp status, final String totalValid) {
        record.setErpId(id);
        record.setTotalValid(totalValid);
        setStatus(record, status);
    }


}
