package ru.cip.ws.erp.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jdbc.entity.ExpSession;
import ru.cip.ws.erp.jdbc.entity.ImpSession;
import ru.cip.ws.erp.jdbc.entity.ImpSessionEvent;

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
        result.setSTART_DATE(now);
        result.setEND_DATE(now);
        result.setENUM_IMP_SESSION_STATUS("DONE");
        result.setSESSION_DESCRIPTION(description);
        result.setSESSION_MSG(message);
        result.setRV(1);
        result.setEXT_PACKAGE_ID(requestId);
        result.setEXT_PACKAGE_CNT(1);
        result.setCREATE_DATE(now);
        result.setUPDATE_DATE(now);
        result.setCREATE_SYSTEM(appID);
        result.setUPDATE_SYSTEM(appID);
        if (exportSesssion != null) {
            result.setEXP_SESSION_ID(exportSesssion.getEXP_SESSION_ID());
        }
        em.persist(result);
        em.flush();
        return result;
    }

    public ImpSessionEvent createNewImportEvent(final String message, final ImpSession imp_session) {
        final Date now = new Date();
        final ImpSessionEvent result = new ImpSessionEvent();
        result.setSYSTEM_ID(appID);
        result.setEVENT_DT(now);
        result.setIMP_SESSION_ID(imp_session.getIMP_SESSION_ID());
        result.setEVENT_TEXT(message);
        result.setEVENT_USER_ID(appID);
        em.persist(result);
        em.flush();
        return result;

    }

    public ImpSession getByEXT_PACKAGE_ID(final String requestId) {
        final List<ImpSession> resultList = em.createQuery("SELECT a FROM ImpSession a WHERE a.EXT_PACKAGE_ID = :requestId", ImpSession.class)
                .setParameter("requestId", requestId).getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }
}
