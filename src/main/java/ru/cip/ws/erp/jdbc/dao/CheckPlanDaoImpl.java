package ru.cip.ws.erp.jdbc.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlan;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Author: Upatov Egor <br>
 * Date: 16.09.2016, 2:35 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */

@Repository
@Transactional
public class CheckPlanDaoImpl {

    @PersistenceContext
    private EntityManager em;

    public List<CipCheckPlan> getAll() {
        return em.createQuery("SELECT a FROM CipCheckPlan a ", CipCheckPlan.class).getResultList();
    }

    public CipCheckPlan getById(final int check_plan_id) {
        return em.find(CipCheckPlan.class, check_plan_id);
    }

    public CipCheckPlan getByYear(final int year) {
        final List<CipCheckPlan> resultList = em.createQuery("SELECT a FROM CipCheckPlan a WHERE a.YEAR = :year", CipCheckPlan.class).setParameter(
                "year", year
        ).getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }
}
