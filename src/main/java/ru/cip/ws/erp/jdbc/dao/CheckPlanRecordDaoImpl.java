package ru.cip.ws.erp.jdbc.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlanRecord;
import ru.cip.ws.erp.jdbc.entity.PlanCheckErp;
import ru.cip.ws.erp.jdbc.entity.PlanCheckRecErp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
       return em.createQuery("SELECT a FROM CipCheckPlanRecord a WHERE a.CHECK_PLAN_ID = :check_plan_id", CipCheckPlanRecord.class)
               .setParameter("check_plan_id", check_plan_id)
               .getResultList();
    }


    public PlanCheckRecErp createPlanCheckRecErp(final PlanCheckErp plan, final CipCheckPlanRecord record){
        final PlanCheckRecErp result = new PlanCheckRecErp();
        result.setIdCheckPlanErp(plan.getIdCheckPlanErp());
        result.setCodeCheckPlanRecErp(null);
        result.setCheckPlanRecStatusErp("WAIT");
        result.setCipChPlRecCorrelId(record.getCorrelationId());
        em.persist(result);
        em.flush();
        return result;
    }

    public void setStatus(final List<PlanCheckRecErp> records, final String status) {
        for (PlanCheckRecErp record : records) {
            setStatus(record, status);
        }
    }

    public void setStatus(final PlanCheckRecErp record, final String status){
        record.setCheckPlanRecStatusErp(status);
        em.merge(record);
        em.flush();
    }

    public List<PlanCheckRecErp> getRecordsByPlanId(final Integer planId) {
        return em.createQuery("SELECT a FROM PlanCheckRecErp a WHERE a.idCheckPlanErp = :plan_id", PlanCheckRecErp.class)
                .setParameter("plan_id", planId).getResultList();
    }
}
