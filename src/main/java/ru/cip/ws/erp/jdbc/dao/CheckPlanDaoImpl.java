package ru.cip.ws.erp.jdbc.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlan;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Author: Upatov Egor <br>
 * Date: 08.10.2016, 17:29 <br>
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

    public CipCheckPlan getById(final int id) {
        return em.find(CipCheckPlan.class, id);
    }

    public List<CipCheckPlan> getByYear(final int year) {
        return em.createQuery("SELECT a FROM CipCheckPlan a WHERE a.year = :year", CipCheckPlan.class).setParameter("year", year).getResultList();
    }
}
