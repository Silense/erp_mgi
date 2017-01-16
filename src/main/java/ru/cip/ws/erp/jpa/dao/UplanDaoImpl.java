package ru.cip.ws.erp.jpa.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jpa.entity.views.Uplan;
import ru.cip.ws.erp.jpa.entity.views.UplanAct;
import ru.cip.ws.erp.jpa.entity.views.UplanActViolation;
import ru.cip.ws.erp.jpa.entity.views.UplanRecord;

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
            if (checkErpDao.notExistsForCheck(uplan)) {
                result.put(uplan, uplan.getRecords());
            }
        }
        return result;
    }

    public Map<Uplan, Set<UplanRecord>> getChecksForFirstAllocationByInterval(
            final Date begDate,
            final Date endDate
    ) {
        final List<Uplan> uplanList = em.createQuery(
                "SELECT p FROM Uplan p " +
                        "LEFT JOIN FETCH p.records r " +
                        "WHERE p.ORDER_DATE >= :begDate " +
                        "AND p.ORDER_DATE < :endDate " +
                        "AND NOT EXISTS (" +
                        "SELECT e.id FROM CheckErp e " +
                        "WHERE e.checkId = p.CHECK_ID " +
                        "AND e.checkType.code = 'UNREGULAR' " +
                        "AND e.state.code <> 'ERROR_ALLOCATED'" +
                        ")",
                Uplan.class)
                .setParameter("begDate", begDate, TemporalType.TIMESTAMP)
                .setParameter("endDate", endDate, TemporalType.TIMESTAMP)
                .getResultList();
        if (uplanList.isEmpty()) {
            return Collections.emptyMap();
        }
        final Map<Uplan, Set<UplanRecord>> result = new LinkedHashMap<>(uplanList.size());
        for (Uplan uplan : uplanList) {
            if (checkErpDao.notExistsForCheck(uplan)) {
                result.put(uplan, uplan.getRecords());
            }
        }
        return result;
    }

    public List<Uplan> getChecksByInterval(final Date begDate, final Date endDate) {
        return em.createQuery(
                "SELECT p FROM Uplan p " +
                        "LEFT JOIN FETCH p.records r " +
                        "WHERE p.ORDER_DATE >= :begDate " +
                        "AND p.ORDER_DATE < :endDate",
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
                "SELECT erp.checkId " +
                        "FROM CheckErp erp " +
                        "WHERE erp.state.code IN :statesForReallocate " +
                        "AND erp.checkType.code = 'UNREGULAR'",
                BigInteger.class
        ).setParameter("statesForReallocate", statesForReallocate).getResultList();
        return getUplanListByIds(needToReallocate);
    }

    private List<Uplan> getUplanListByIds(List<BigInteger> needToReallocate) {
        if (needToReallocate.isEmpty()) {
            return Collections.emptyList();
        } else {
            return em.createQuery(
                    "SELECT p FROM Uplan p " +
                            "LEFT JOIN FETCH p.records r " +
                            "WHERE p.id IN :ids",
                    Uplan.class)
                    .setParameter("ids", needToReallocate)
                    .getResultList();
        }
    }

    public List<Uplan> getAllocated() {
        final List<String> stateAllocated = new ArrayList<>(2);
        stateAllocated.add("ALLOCATED");
        stateAllocated.add("ERROR_RESULT_ALLOCATION");
        final List<BigInteger> needToProcess = em.createQuery(
                "SELECT erp.checkId " +
                        "FROM CheckErp erp " +
                        "WHERE erp.state.code IN :stateAllocated " +
                        "AND erp.checkType.code = 'UNREGULAR'",
                BigInteger.class
        ).setParameter("stateAllocated", stateAllocated).getResultList();
        return getUplanListByIds(needToProcess);
    }

    public Map<UplanAct, Set<UplanActViolation>> getViolations(Uplan check) {
        final List<UplanAct> resultList = em.createQuery(
                "SELECT a FROM UplanAct a WHERE a.check.id = :checkId ", UplanAct.class
        ).setParameter("checkId", check.getCHECK_ID()).getResultList();
        if(resultList.isEmpty()){
            return Collections.emptyMap();
        } else {
            final Map<UplanAct, Set<UplanActViolation>> result = new LinkedHashMap<>(resultList.size());
            for (UplanAct act : resultList) {
               result.put(act, getViolations(check, act));
            }
            return result;
        }
    }

    private Set<UplanActViolation> getViolations(Uplan check, UplanAct act) {
        final List<UplanActViolation> result = em.createQuery(
                "SELECT a FROM UplanActViolation a WHERE a.check.id = :checkId ", UplanActViolation.class
        ).setParameter("checkId", check.getCHECK_ID()).getResultList();
        return new LinkedHashSet<>(result);
    }
}
