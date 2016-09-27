package ru.cip.ws.erp.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * Author: Upatov Egor <br>
 * Date: 11.09.2016, 17:54 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */

@Component
public class PropertiesHolder {


    @Value("${mgi.org.name}")
    public String MGI_ORG_NAME;

    @Value("${messagetype.info_model}")
    public BigInteger MESSAGETYPE_INFO_MODEL;

    @Value("${messagetype.previous_info_model}")
    public BigInteger MESSAGETYPE_PREVIOUS_INFO_MODEL;

    @Value("${ko.name}")
    public String KO_NAME;

    @Value("${mgi.org.orgn}")
    public String MGI_OGRN;

    @Value("${mgi.org.frgu_serv_id}")
    public long MGI_FRGU_SERV_ID;

    @Value("${mgi.org.frgu_org_id}")
    public long MGI_FRGU_ORG_ID;

    @Value("${addresseetype.code}")
    public String ADDRESSEE_CODE;

    @Value("${addresseetype.name}")
    public String ADDRESSEE_NAME;
}
