package ru.cip.ws.erp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.cip.ws.erp.factory.MessageFactory;
import ru.cip.ws.erp.generated.erptypes.MessageToERPModelType;
import ru.cip.ws.erp.jpa.dao.SystemSettingsDaoImpl;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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



    @Autowired
    private SystemSettingsDaoImpl settingsDao;

    public static final String CFG_KEY_APP_ID = "app.id";
    public static final String CFG_KEY_DATASOURCE_JNDI = "dbcp.jndiName";

    /**
     * Настройки расписаний выгрузки внеплановых проверок
     */
    public static final String CFG_KEY_UNREGULAR_SCHEDULE = "unregular.schedule";
    public static final String CFG_KEY_UNREGULAR_SCHEDULE_LAST_FIRE_DATE = "unregular.schedule.lastFireDate";
    public static final String CFG_KEY_UNREGULAR_RESULT_SCHEDULE = "unregularResult.schedule";
    public static final String CFG_KEY_UNREGULAR_RESULT_SCHEDULE_LAST_FIRE_DATE = "unregularResult.schedule.lastFireDate";

    /**
     * Мейлер
     */
    private static final String CFG_KEY_MGI_NAME = "mgi.org.name";
    private static final String CFG_KEY_MGI_OGRN = "mgi.org.ogrn";
    private static final String CFG_KEY_MGI_FRGU_ORG_ID = "mgi.org.frgu_org_id";
    private static final String CFG_KEY_MGI_FRGU_SERV_ID = "mgi.org.frgu_serv_id";

    private static final String CFG_KEY_ADDRESSEE_CODE = "addressee.code";
    private static final String CFG_KEY_ADDRESSEE_NAME = "addressee.name";

    /**
     * Настройки очередей
     */
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
    public ConfigurationHolder( @Qualifier("app.properties") final Properties props) {
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            properties.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
    }

    @PostConstruct
    public void afterPropertiesSet(){
        properties.putAll(settingsDao.getSettings(get(CFG_KEY_APP_ID)));
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

    public Date getDate(String key) {
        final Object result = properties.get(key);
        if(result instanceof Date){
            return (Date) result;
        }
        try {
            return result != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(result.toString()) : null;
        } catch (ParseException e) {
            return null;
        }
    }

    public boolean set(String key, String value) {
        return properties.containsKey(key) && settingsDao.setNewStringValue(get(CFG_KEY_APP_ID), key, value);
    }

    public boolean set(String key, Date value) {
        return properties.containsKey(key) && settingsDao.setNewDateValue(get(CFG_KEY_APP_ID), key, value);
    }

    private BigInteger getNumber(String key) {
        final Object result = properties.get(key);
        if(result instanceof BigInteger){
            return (BigInteger) result;
        }
        try {
            return result != null ? new BigInteger(result.toString()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("============================================================\n");
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append('\n');
        }
        return sb.toString();
    }

    public MessageToERPModelType.Mailer getMailer() {
        return MessageFactory.createMailer(
                get(CFG_KEY_MGI_NAME),
                get(CFG_KEY_MGI_OGRN),
                getNumber(CFG_KEY_MGI_FRGU_ORG_ID),
                getNumber(CFG_KEY_MGI_FRGU_SERV_ID)
        );
    }

    public MessageToERPModelType.Addressee getAddressee() {
        return MessageFactory.createAddressee(
                get(CFG_KEY_ADDRESSEE_CODE),
                get(CFG_KEY_ADDRESSEE_NAME)
        );
    }



}
