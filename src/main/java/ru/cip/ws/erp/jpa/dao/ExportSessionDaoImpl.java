package ru.cip.ws.erp.jpa.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.factory.PropertiesHolder;
import ru.cip.ws.erp.jpa.entity.enums.SessionStatus;
import ru.cip.ws.erp.jpa.entity.sessions.ExpSession;
import ru.cip.ws.erp.jpa.entity.sessions.ExpSessionEvent;

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

    @Autowired
    private PropertiesHolder props;


    @PersistenceContext
    private EntityManager em;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ExpSession createExportSession(final Date date, final String description, final String message, final int count) {
        final ExpSession result = new ExpSession();
        result.setSYSTEM_SERVICE_ID(props.APP_ID);
        result.setSTART_DATE(date);
        result.setEND_DATE(null);
        result.setENUM_EXP_SESSION_STATUS(SessionStatus.RUNNING);
        result.setSESSION_DESCRIPTION(description);
        result.setSESSION_MSG(message);
        result.setRV(1);
        result.setEXT_PACKAGE_ID(null);
        result.setEXT_PACKAGE_CNT(count);
        result.setCREATE_DATE(date);
        result.setUPDATE_DATE(date);
        result.setCREATE_SYSTEM(props.APP_ID);
        result.setUPDATE_SYSTEM(props.APP_ID);
        em.persist(result);
        return result;
    }

    public ExpSessionEvent createExportEvent(final Date date, final String message, final ExpSession exp_session) {
        final ExpSessionEvent result = new ExpSessionEvent();
        result.setEventDateTime(date);
        result.setEVENT_TEXT(message);
        result.setEVENT_USER_ID(props.APP_ID);
        result.setExportSession(exp_session);
        em.persist(result);
        return result;

    }

    public ExpSession getSessionByEXT_PACKAGE_ID(final String uuid) {
        final List<ExpSession> resultList = em.createQuery("SELECT a FROM ExpSession a WHERE a.EXT_PACKAGE_ID = :ext_package_id", ExpSession.class)
                .setParameter("ext_package_id", uuid).getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }

    public void setSessionInfo(final ExpSession session, final SessionStatus status, final String message) {
        session.setSESSION_MSG(message);
        setSessionStatus(session, status);
    }

    public void setSessionStatus(final ExpSession session, final SessionStatus status) {
        session.setEND_DATE(new Date());
        session.setENUM_EXP_SESSION_STATUS(status);
        em.merge(session);
    }

    public ExpSession getSessionById(final Integer id) {
        return em.find(ExpSession.class, id);
    }

    public ExpSession merge(final ExpSession expSession) {
        return em.merge(expSession);
    }

    public Tuple<ExpSession, ExpSessionEvent> createExportSessionInfo(final Date date, final String description, final String messageType, final int count) {
        final ExpSession session = createExportSession(date, description, messageType, count);
        final ExpSessionEvent event = createExportEvent(date, messageType, session);
        return new Tuple<>(session, event);
    }
}
