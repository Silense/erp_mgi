package ru.cip.ws.erp.jdbc.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jdbc.entity.views.PlanAct;
import ru.cip.ws.erp.jdbc.entity.views.PlanActViolation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Author: Upatov Egor <br>
 * Date: 11.10.2016, 11:59 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
@Transactional
public class PlanActViolationDaoImpl {

    @PersistenceContext
    private EntityManager em;


    public List<PlanActViolation> getByAct(final PlanAct act) {
        return em.createQuery("SELECT v FROM PlanActViolation v WHERE v.ACT_ID = :act_id", PlanActViolation.class).setParameter(
                "act_id", act.getACT_ID()
        ).getResultList();
    }
}
