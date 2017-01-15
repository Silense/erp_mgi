package ru.cip.ws.erp.jpa.dao;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.jpa.entity.RsysSystemServiceSetting;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
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
    private static final Logger log = LoggerFactory.getLogger("CONFIG");

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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean setNewStringValue(String systemName, String key, String value) {
        final RsysSystemServiceSetting settingToChange = getSetting(systemName, key);
        if (settingToChange == null) {
            log.warn("No settings with key[{}] to system[{}] found", key, systemName);
            return false;
        } else {
            final String settingToChangeToString = settingToChange.toString();
            settingToChange.setValueString(value);
            final RsysSystemServiceSetting merged = em.merge(settingToChange);
            log.warn("Changed from {} to {}", settingToChangeToString, merged);
            return true;
        }
    }

    private RsysSystemServiceSetting getSetting(String systemName, String key) {
        final List<RsysSystemServiceSetting> resultList = em.createQuery(
                "SELECT s FROM RsysSystemServiceSetting s WHERE s.system = :systemName AND s.settingId = :key",
                RsysSystemServiceSetting.class
        )
                .setParameter("key", key)
                .setParameter("systemName", systemName)
                .getResultList();
        return resultList.iterator().hasNext() ? resultList.iterator().next() : null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean setNewDateValue(String systemName, String key, Date value) {
        final RsysSystemServiceSetting settingToChange = getSetting(systemName, key);
        if (settingToChange == null) {
            log.warn("No settings with key[{}] to system[{}] found", key, systemName);
            return false;
        } else {
            final String settingToChangeToString = settingToChange.toString();
            settingToChange.setValueDate(value);
            final RsysSystemServiceSetting merged = em.merge(settingToChange);
            log.warn("Changed from {} to {}", settingToChangeToString, merged);
            return true;
        }
    }
}
