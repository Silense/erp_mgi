package ru.cip.ws.erp.jdbc.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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

    @SuppressWarnings("unchecked")
    public List<CipCheckPlanRecord> getRecordsByPlanId(final int check_plan_id) {
       return em.createQuery("SELECT a FROM CipCheckPlanRecord a WHERE a.CHECK_PLAN_ID = :check_plan_id", CipCheckPlanRecord.class)
               .setParameter("check_plan_id", check_plan_id)
               .getResultList();
    }
}
