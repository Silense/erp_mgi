package ru.cip.ws.erp.jdbc.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlan;
import ru.cip.ws.erp.jdbc.entity.ExpSession;
import ru.cip.ws.erp.jdbc.entity.PlanCheckErp;

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

    public List<CipCheckPlan> getAllFromView() {
        return em.createQuery("SELECT a FROM CipCheckPlan a ", CipCheckPlan.class).getResultList();
    }

    public CipCheckPlan getByIdFromView(final int check_plan_id) {
        return em.find(CipCheckPlan.class, check_plan_id);
    }

    public CipCheckPlan getByYearFromView(final int year) {
        final List<CipCheckPlan> resultList = em.createQuery("SELECT a FROM CipCheckPlan a WHERE a.YEAR = :year", CipCheckPlan.class).setParameter(
                "year", year
        ).getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }


    public PlanCheckErp createPlanCheckErp(final int id, final Integer prosecutorId, final ExpSession expSession){
        final PlanCheckErp result = new PlanCheckErp();
        result.setIdCheckPlanErp(id);
        result.setIdProsecutors(prosecutorId);
        result.setCodeCheckPlanErp(null);
        result.setCheckPlanStatusErp("WAIT");
        result.setCipChPlLglApprvdId(null);
        result.setExpSessionId(expSession.getEXP_SESSION_ID());
        em.persist(result);
        return result;
    }

    public void setStatus(final PlanCheckErp planCheckErp, final String status) {
        planCheckErp.setCheckPlanStatusErp(status);
        em.merge(planCheckErp);
    }
}
