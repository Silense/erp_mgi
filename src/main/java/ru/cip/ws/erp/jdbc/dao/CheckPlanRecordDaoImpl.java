package ru.cip.ws.erp.jdbc.dao;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Component;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlanRecord;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Upatov Egor <br>
 * Date: 11.09.2016, 16:03 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Component
public class CheckPlanRecordDaoImpl {

    @Resource
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public List<CipCheckPlanRecord> getAllRecords() {
        Session session = null;
        List<CipCheckPlanRecord> result = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            result = session.createQuery("SELECT a FROM CipCheckPlanRecord a ").list();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<CipCheckPlanRecord> getRecordsByPlanId(final int check_plan_id) {
        Session session = null;
        List<CipCheckPlanRecord> result = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            result = session.createQuery("SELECT a FROM CipCheckPlanRecord a WHERE a.CHECK_PLAN_ID = :check_plan_id")
                    .setParameter("check_plan_id", check_plan_id)
                    .list();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }
}
