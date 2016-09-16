package ru.cip.ws.erp.jdbc.dao;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Component;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlan;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlanRecord;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Upatov Egor <br>
 * Date: 16.09.2016, 2:35 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */

@Component
public class CheckPlanDaoImpl {
    @Resource
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public List<CipCheckPlan> getAll() {
        Session session = null;
        List<CipCheckPlan> result = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            result = session.createQuery("SELECT a FROM CipCheckPlan a ").list();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
        public CipCheckPlan getById(final int check_plan_id) {
        Session session = null;
        CipCheckPlan result = null;
        try {
            session = sessionFactory.openSession();
            List resultList = session.createQuery("SELECT a FROM CipCheckPlan a WHERE a.CHECK_PLAN_ID = :check_plan_id")
                    .setParameter("check_plan_id", check_plan_id)
                    .list();
            result = resultList.iterator().hasNext() ? (CipCheckPlan) resultList.iterator().next() : null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public CipCheckPlan getByYear(final int year) {
        Session session = null;
        CipCheckPlan result = null;
        try {
            session = sessionFactory.openSession();
            List resultList = session.createQuery("SELECT a FROM CipCheckPlan a WHERE a.YEAR = :year")
                    .setParameter("year", year)
                    .list();
            result = resultList.iterator().hasNext() ? (CipCheckPlan) resultList.iterator().next() : null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }
}
