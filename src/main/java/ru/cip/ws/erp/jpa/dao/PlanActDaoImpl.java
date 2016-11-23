package ru.cip.ws.erp.jpa.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jpa.entity.views.Plan;
import ru.cip.ws.erp.jpa.entity.views.PlanAct;
import ru.cip.ws.erp.jpa.entity.views.PlanActViolation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.*;

/**
 * Author: Upatov Egor <br>
 * Date: 08.10.2016, 22:49 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
@Transactional
public class PlanActDaoImpl {

    @Autowired
    private PlanActViolationDaoImpl violationDao;

    @PersistenceContext
    private EntityManager em;


    public List<PlanAct> getByPlan(final Plan plan) {
        return em.createQuery("SELECT a FROM PlanAct a WHERE a.CHECK_ID = :plan_id", PlanAct.class).setParameter(
                "plan_id", BigInteger.valueOf(plan.getId())
        ).getResultList();
    }

    public Map<PlanAct, Set<PlanActViolation>> getWithViolationsByPlan(final Plan plan) {
        final List<PlanAct> actList = getByPlan(plan);
        final Map<PlanAct, Set<PlanActViolation>> result = new HashMap<>(actList.size());
        for (PlanAct currentAct : actList) {
            result.put(currentAct, new LinkedHashSet<PlanActViolation>(violationDao.getByAct(currentAct)));
        }
        return result;
    }
}
