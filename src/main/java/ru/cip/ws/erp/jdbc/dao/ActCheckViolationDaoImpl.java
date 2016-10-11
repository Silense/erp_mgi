package ru.cip.ws.erp.jdbc.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jdbc.entity.CipActCheck;
import ru.cip.ws.erp.jdbc.entity.CipActCheckViolation;

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
public class ActCheckViolationDaoImpl {
    @PersistenceContext
    private EntityManager em;


    public List<CipActCheckViolation> getByAct(final CipActCheck act) {
        return em.createQuery("SELECT v FROM CipActCheckViolation v WHERE v.ACT_ID = :act_id", CipActCheckViolation.class).setParameter(
                "act_id", act.getACT_ID()
        ).getResultList();
    }
}
