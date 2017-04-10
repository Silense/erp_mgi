package ru.cip.ws.erp.jpa.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jpa.entity.views.Uplan;
import ru.cip.ws.erp.jpa.entity.views.UplanAct;
import ru.cip.ws.erp.jpa.entity.views.UplanActViolation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.math.BigInteger;
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

    public List<Uplan> getChecksByInterval(final Date begDate, final Date endDate) {
        return em.createQuery(
                "SELECT distinct p FROM Uplan p LEFT JOIN FETCH p.records r " +
                        "WHERE p.ORDER_DATE >= :begDate AND p.ORDER_DATE < :endDate",
                Uplan.class)
                .setParameter("begDate", begDate, TemporalType.TIMESTAMP)
                .setParameter("endDate", endDate, TemporalType.TIMESTAMP)
                .getResultList();
    }

    public List<Uplan> getPartialAllocated() {
        final List<String> statesForReallocate = new ArrayList<>(2);
        statesForReallocate.add("PARTIAL_ALLOCATION");
        statesForReallocate.add("ERROR_ALLOCATION");
        final List<BigInteger> needToReallocate = em.createQuery(
                "SELECT erp.checkId FROM CheckErp erp " +
                        "WHERE erp.state.code IN :statesForReallocate AND erp.checkType.code = 'UNREGULAR'",
                BigInteger.class
        ).setParameter("statesForReallocate", statesForReallocate).getResultList();
        return getUplanListByIds(needToReallocate);
    }

    private List<Uplan> getUplanListByIds(List<BigInteger> needToReallocate) {
        if (needToReallocate.isEmpty()) {
            return Collections.emptyList();
        } else {
            return em.createQuery("SELECT distinct p FROM Uplan p LEFT JOIN FETCH p.records r WHERE p.id IN :ids", Uplan.class)
                    .setParameter("ids", needToReallocate).getResultList();
        }
    }

    public List<Uplan> getAllocated() {
        final List<String> stateAllocated = new ArrayList<>(2);
        stateAllocated.add("ALLOCATED");
        stateAllocated.add("ERROR_RESULT_ALLOCATION");
        final List<BigInteger> needToProcess = em.createQuery(
                "SELECT erp.checkId FROM CheckErp erp " +
                        "WHERE erp.state.code IN :stateAllocated AND erp.checkType.code = 'UNREGULAR'",
                BigInteger.class
        ).setParameter("stateAllocated", stateAllocated).getResultList();
        return getUplanListByIds(needToProcess);
    }

    public Uplan getByOrderNum(String orderNum) {
        final List<Uplan> resultList = em.createQuery(
                "SELECT distinct p FROM Uplan p LEFT JOIN FETCH p.records r WHERE p.ORDER_NUM = :orderNum", Uplan.class
        ).setParameter("orderNum", orderNum).getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }

    public Uplan getByOrderNumAndOrderDate(String orderNum, Date orderDate) {
        final List<Uplan> resultList = em.createQuery(
                "SELECT distinct p FROM Uplan p LEFT JOIN FETCH p.records r WHERE p.ORDER_NUM = :orderNum AND p.ORDER_DATE = :orderDate", Uplan.class
        ).setParameter("orderNum", orderNum).setParameter("orderDate", orderDate).getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }

    public Set<UplanActViolation> getViolations(Uplan check, UplanAct act) {
        final List<UplanActViolation> result = em.createQuery(
                "SELECT a FROM UplanActViolation a WHERE a.check.id = :checkId ", UplanActViolation.class
        ).setParameter("checkId", check.getCHECK_ID()).getResultList();
        return new LinkedHashSet<>(result);
    }


    public UplanAct getAct(Uplan check) {
        final List<UplanAct> resultList = em.createQuery(
                "SELECT a FROM UplanAct a WHERE a.check.id = :checkId ", UplanAct.class
        ).setParameter("checkId", check.getCHECK_ID()).getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }


    public List<Uplan> getUnAllocatedChecksFromDate(Date begDate) {
        return em.createQuery(
                "SELECT distinct p " +
                        "FROM Uplan p " +
                        "LEFT JOIN FETCH p.records r " +
                        "WHERE p.ORDER_DATE >= :begDate " +
                        "AND NOT EXISTS (" +
                        "   SELECT p.id FROM CheckErp c WHERE c.checkId = p.CHECK_ID " +
                        ")",
                Uplan.class)
                .setParameter("begDate", begDate, TemporalType.TIMESTAMP)
                .getResultList();
    }
}
