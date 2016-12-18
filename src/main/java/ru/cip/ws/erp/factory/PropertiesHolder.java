package ru.cip.ws.erp.factory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Properties;

/**
 * Author: Upatov Egor <br>
 * Date: 11.09.2016, 17:54 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */

@Component("propertiesHolder")
public class PropertiesHolder {

    @Autowired
    public PropertiesHolder(@Qualifier("app.properties") Properties props) {
        APP_ID = props.getProperty("app.id");
        MGI_ORG_NAME = props.getProperty("mgi.org.name");
        if (StringUtils.isNotEmpty(props.getProperty("messagetype.info_model"))) {
            MESSAGETYPE_INFO_MODEL = new BigInteger(props.getProperty("messagetype.info_model"));
        } else {
            MESSAGETYPE_INFO_MODEL = null;
        }
        if (StringUtils.isNotEmpty(props.getProperty("messagetype.previous_info_model"))) {
            MESSAGETYPE_PREVIOUS_INFO_MODEL = new BigInteger(props.getProperty("messagetype.previous_info_model"));
        } else {
            MESSAGETYPE_PREVIOUS_INFO_MODEL = null;
        }
        KO_NAME = props.getProperty("ko.name");
        MGI_OGRN = props.getProperty("mgi.org.orgn");
        MGI_FRGU_SERV_ID = Long.parseLong(props.getProperty("mgi.org.frgu_serv_id"));
        MGI_FRGU_ORG_ID = Long.parseLong(props.getProperty("mgi.org.frgu_org_id"));
        ADDRESSEE_CODE = props.getProperty("addresseetype.code");
        ADDRESSEE_NAME = props.getProperty("addresseetype.name");
    }

    public final String MGI_ORG_NAME;
    public final BigInteger MESSAGETYPE_INFO_MODEL;
    public final BigInteger MESSAGETYPE_PREVIOUS_INFO_MODEL;
    public final String KO_NAME;
    public final String MGI_OGRN;
    public final long MGI_FRGU_SERV_ID;
    public final long MGI_FRGU_ORG_ID;
    public final String ADDRESSEE_CODE;
    public final String ADDRESSEE_NAME;
    public final String APP_ID;
}
