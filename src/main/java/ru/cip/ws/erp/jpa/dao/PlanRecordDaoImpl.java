package ru.cip.ws.erp.jpa.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jpa.entity.views.Plan;
import ru.cip.ws.erp.jpa.entity.views.PlanRecord;

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
public class PlanRecordDaoImpl {

    @PersistenceContext
    private EntityManager em;

    public List<PlanRecord> getAllRecords() {
        return em.createQuery("SELECT a FROM PlanRecord a ", PlanRecord.class).getResultList();
    }

    public List<PlanRecord> getRecordsByPlan(final Plan plan) {
        return em.createQuery("SELECT a FROM PlanRecord a WHERE a.plan.id = :plan_id", PlanRecord.class)
                .setParameter("plan_id", plan.getId()).getResultList();
    }

}
