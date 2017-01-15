package ru.cip.ws.erp.jpa.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jpa.entity.views.Uplan;
import ru.cip.ws.erp.jpa.entity.views.UplanRecord;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.util.*;

/**
 * Author: Upatov Egor <br>
 * Date: 12.01.2017, 20:01 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
@Transactional
public class UplanDaoImpl {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CheckErpDaoImpl checkErpDao;

    public Map<Uplan, Set<UplanRecord>> getUnallocatedChecksByInterval(Date begDate, Date endDate) {
        final List<Uplan> uplanList = em.createQuery(
                "SELECT p FROM Uplan p " +
                        "LEFT JOIN FETCH p.records r " +
                        "WHERE p.ORDER_DATE >= :begDate " +
                        "AND p.ORDER_DATE < :endDate",
                Uplan.class)
                .setParameter("begDate", begDate, TemporalType.TIMESTAMP)
                .setParameter("endDate", endDate, TemporalType.TIMESTAMP)
                .getResultList();
        if (uplanList.isEmpty()) {
            return Collections.emptyMap();
        }
        final Map<Uplan, Set<UplanRecord>> result = new LinkedHashMap<>(uplanList.size());
        for (Uplan uplan : uplanList) {
            if(checkErpDao.notExistsForCheck(uplan)) {
                result.put(uplan, uplan.getRecords());
            }
        }
        return result;
    }
}
