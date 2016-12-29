package ru.cip.ws.erp.jpa.dao;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jpa.entity.RsysSystemServiceSetting;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Upatov Egor <br>
 * Date: 29.12.2016, 17:16 <br>
 * Description: Дао для работы с системными настройками
 */
@Repository
@Transactional
public class SystemSettingsDaoImpl {
    @PersistenceContext
    private EntityManager em;

    public Map<String, Object> getSettings(final String systemName) {
        final Map<String, Object> result = new HashMap<>();
        final List<RsysSystemServiceSetting> resultList = em.createQuery(
                "SELECT s FROM RsysSystemServiceSetting s WHERE s.system = :systemName", RsysSystemServiceSetting.class
        )
                .setParameter("systemName", systemName)
                .getResultList();
        for (RsysSystemServiceSetting item : resultList) {
            Object value = ObjectUtils.firstNonNull(item.getValueString(), item.getValueNumber(), item.getValueDate());
            result.put(item.getSettingId(), value);
        }
        return result;
    }
}
