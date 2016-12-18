package ru.cip.ws.erp.jpa.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jpa.entity.RsysEnum;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Author: Upatov Egor <br>
 * Date: 18.12.2016, 5:03 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */

@Repository
@Transactional
public class EnumDaoImpl {

    @PersistenceContext
    private EntityManager em;


    public RsysEnum get(final String catalogId, final String code) {
        return em.createQuery("SELECT a FROM RsysEnum a WHERE a.catalog.id = :catalogId AND a.code = :code", RsysEnum.class)
                .setParameter("catalogId",catalogId)
                .setParameter("code", code)
                .getSingleResult();
    }
}
