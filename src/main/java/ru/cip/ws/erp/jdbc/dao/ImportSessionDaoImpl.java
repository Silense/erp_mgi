package ru.cip.ws.erp.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jdbc.entity.sessions.ExpSession;
import ru.cip.ws.erp.jdbc.entity.sessions.ImpSession;
import ru.cip.ws.erp.jdbc.entity.sessions.ImpSessionEvent;
import ru.cip.ws.erp.jdbc.entity.enums.SessionStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Author: Upatov Egor <br>
 * Date: 16.09.2016, 4:17 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
@Transactional
public class ImportSessionDaoImpl {

    private static final Logger logger = LoggerFactory.getLogger(ImportSessionDaoImpl.class);

    @Value("${app.id}")
    private String appID;

    @Autowired
    private ExportSessionDaoImpl exportSessionDao;

    @PersistenceContext
    private EntityManager em;


    public ImpSession createNewImportSession(final String description, final String message, final String requestId) {
        final Date now = new Date();
        final ExpSession exportSesssion = exportSessionDao.getSessionByEXT_PACKAGE_ID(requestId);
        logger.debug("For new ImportSession founded export session: {}", exportSesssion);
        final ImpSession result = new ImpSession();
        result.setSYSTEM_SERVICE_ID(appID);
        result.setSYSTEM_ID(appID);
        result.setStartDate(now);
        result.setEndDate(now);
        result.setENUM_IMP_SESSION_STATUS(SessionStatus.DONE);
        result.setSESSION_DESCRIPTION(description);
        result.setSESSION_MSG(message);
        result.setRV(1);
        result.setEXT_PACKAGE_ID(requestId);
        result.setEXT_PACKAGE_CNT(1);
        result.setCreateDate(now);
        result.setUpdateDate(now);
        result.setCREATE_SYSTEM(appID);
        result.setUPDATE_SYSTEM(appID);
        if (exportSesssion != null) {
            result.setExportSession(exportSesssion);
        }
        em.persist(result);
        return result;
    }

    public ImpSessionEvent createNewImportEvent(final String message, final ImpSession imp_session) {
        final Date now = new Date();
        final ImpSessionEvent result = new ImpSessionEvent();
        result.setSYSTEM_ID(appID);
        result.setEventDateTime(now);
        result.setImportSession(imp_session);
        result.setEVENT_TEXT(message);
        result.setEVENT_USER_ID(appID);
        em.persist(result);
        return result;

    }

    public ImpSession getByEXT_PACKAGE_ID(final String requestId) {
        final List<ImpSession> resultList = em.createQuery("SELECT a FROM ImpSession a WHERE a.EXT_PACKAGE_ID = :requestId", ImpSession.class)
                .setParameter("requestId", requestId).getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }
}
