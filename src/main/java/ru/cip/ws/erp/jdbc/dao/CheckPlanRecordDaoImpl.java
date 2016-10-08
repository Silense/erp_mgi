package ru.cip.ws.erp.jdbc.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlan;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlanRecord;

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

    public List<CipCheckPlanRecord> getAllRecords() {
        return em.createQuery("SELECT a FROM CipCheckPlanRecord a ", CipCheckPlanRecord.class).getResultList();
    }

    public List<CipCheckPlanRecord> getRecordsByPlan(final CipCheckPlan plan) {
        return em.createQuery("SELECT a FROM CipCheckPlanRecord a WHERE a.plan.id = :plan_id", CipCheckPlanRecord.class)
                .setParameter("plan_id", plan.getId()).getResultList();
    }

}
