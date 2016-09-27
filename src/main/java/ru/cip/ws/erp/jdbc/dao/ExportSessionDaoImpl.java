package ru.cip.ws.erp.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jdbc.entity.ExpSession;
import ru.cip.ws.erp.jdbc.entity.ExpSessionEvent;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Author: Upatov Egor <br>
 * Date: 16.09.2016, 4:18 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
@Transactional
public class ExportSessionDaoImpl {

    private static final Logger logger = LoggerFactory.getLogger(ExportSessionDaoImpl.class);

    @Value("${app.id}")
    private String appID;


    @PersistenceContext
    private EntityManager em;


    public ExpSession createExportSession(final String description, final String message, final String requestId) {
        final Date now = new Date();
        final ExpSession result = new ExpSession();
        result.setSYSTEM_SERVICE_ID(appID);
        result.setSTART_DATE(now);
        result.setEND_DATE(now);
        result.setENUM_EXP_SESSION_STATUS("DONE");
        result.setSESSION_DESCRIPTION(description);
        result.setSESSION_MSG(message);
        result.setRV(1);
        result.setEXT_PACKAGE_ID(requestId);
        result.setEXT_PACKAGE_CNT(1);
        result.setCREATE_DATE(now);
        result.setUPDATE_DATE(now);
        result.setCREATE_SYSTEM(appID);
        result.setUPDATE_SYSTEM(appID);
        em.persist(result);
        em.flush();
        return result;
    }

    public ExpSessionEvent createExportEvent(final String message, final ExpSession exp_session) {
        final Date now = new Date();
        final ExpSessionEvent result = new ExpSessionEvent();
        result.setEVENT_DT(now);
        result.setEVENT_TEXT(message);
        result.setEVENT_USER_ID(appID);
        result.setEXP_SESSION_ID(exp_session.getEXP_SESSION_ID());
        em.persist(result);
        em.flush();
        return result;

    }

    public ExpSession getSessionByEXT_PACKAGE_ID(final String ext_package_id) {
        final List<ExpSession> resultList = em.createQuery("SELECT a FROM ExpSession a WHERE a.EXT_PACKAGE_ID = :ext_pakage_id", ExpSession.class)
                .setParameter("ext_pakage_id", ext_package_id)
                .getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }

    public void setSessionInfo(final ExpSession session, final String status, final String message) {
        session.setSESSION_MSG(message);
        setSessionStatus(session, status);
    }

    public void setSessionStatus(final ExpSession session, final String status) {
        session.setEND_DATE(new Date());
        session.setENUM_EXP_SESSION_STATUS(status);
        em.merge(session);
        em.flush();
    }

    public ExpSession getSessionById(final Integer id) {
        return em.find(ExpSession.class, id);
    }

    public ExpSession merge(final ExpSession expSession) {
        return em.merge(expSession);
    }
}
