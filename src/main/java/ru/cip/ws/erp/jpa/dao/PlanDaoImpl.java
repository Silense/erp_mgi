package ru.cip.ws.erp.jpa.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jpa.entity.views.Plan;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Author: Upatov Egor <br>
 * Date: 08.10.2016, 17:29 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */


@Repository
@Transactional
public class PlanDaoImpl {

    @PersistenceContext
    private EntityManager em;

    public Set<Plan> getAll() {
        return new LinkedHashSet<>(em.createQuery("SELECT a FROM Plan a ", Plan.class).getResultList());
    }

    public Plan getById(final int id) {
        return em.find(Plan.class, id);
    }

    public Set<Plan> getByYear(final int year) {
        return new LinkedHashSet<>(em.createQuery("SELECT a FROM Plan a WHERE a.year = :year", Plan.class).setParameter("year", year).getResultList());
    }

}
