package ru.cip.ws.erp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.cip.ws.erp.jpa.dao.SystemSettingsDaoImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Author: Upatov Egor <br>
 * Date: 29.12.2016, 15:21 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Component("configurationHolder")
public class ConfigurationHolder {
    private static final Logger log = LoggerFactory.getLogger("CONFIG");

    public static final String CFG_KEY_APP_ID = "app.id";
    public static final String CFG_KEY_DATASOURCE_JNDI = "dbcp.jndiName";

    public static final String CFG_KEY_QUEUE_REQUEST = "wmq.queue.request";
    public static final String CFG_KEY_QUEUE_RESPONSE = "wmq.queue.response";
    public static final String CFG_KEY_MQ_LOGIN = "wmq.qmgr.login";
    public static final String CFG_KEY_MQ_PASSWORD = "wmq.qmgr.password";
    public static final String CFG_KEY_CCSID = "wmq.qmgr.ccid";
    public static final String CFG_KEY_MQ_CHANNEL ="wmq.qmgr.channel";
    public static final String CFG_KEY_MQ_HOSTS ="wmq.qmgr.hosts";
    public static final String CFG_KEY_MQ_NAME ="wmq.qmgr.name";


    private static final Map<String, Object> properties = new HashMap<>();

    @Autowired
    public ConfigurationHolder(
            @Qualifier("app.properties") final Properties props,
            final SystemSettingsDaoImpl settingsDao) {
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            properties.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        properties.putAll(settingsDao.getSettings(props.getProperty(CFG_KEY_APP_ID)));
        log.info(this.toString());
    }

    public String get(String key) {
        final Object result = properties.get(key);
        return result != null ? result.toString() : null;
    }

    public Integer getInteger(String key) {
        final Object result = properties.get(key);
        return result != null ? Integer.valueOf(result.toString()) : null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("============================================================\n");
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append('\n');
        }
        return sb.toString();
    }
}
