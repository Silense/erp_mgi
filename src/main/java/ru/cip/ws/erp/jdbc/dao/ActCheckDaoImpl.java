package ru.cip.ws.erp.jdbc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jdbc.entity.CipActCheck;
import ru.cip.ws.erp.jdbc.entity.CipActCheckViolation;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlan;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Upatov Egor <br>
 * Date: 08.10.2016, 22:49 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
@Transactional
public class ActCheckDaoImpl {

    @Autowired
    private ActCheckViolationDaoImpl violationDao;

    @PersistenceContext
    private EntityManager em;


    public List<CipActCheck> getByPlan(final CipCheckPlan plan) {
        return em.createQuery("SELECT a FROM CipActCheck a WHERE a.CHECK_ID = :plan_id", CipActCheck.class).setParameter(
                "plan_id", BigInteger.valueOf(plan.getId())
        ).getResultList();
    }

    public Map<CipActCheck, List<CipActCheckViolation>> getWithViolationsByPlan(final CipCheckPlan plan) {
        final List<CipActCheck> actList = getByPlan(plan);
        final Map<CipActCheck, List<CipActCheckViolation>> result = new HashMap<>(actList.size());
        for (CipActCheck currentAct : actList) {
            result.put(currentAct, violationDao.getByAct(currentAct));
        }
        return result;
    }
}
