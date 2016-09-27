package ru.cip.ws.erp.factory;

import org.apache.commons.lang3.StringUtils;

/**
 * Author: Upatov Egor <br>
 * Date: 11.09.2016, 13:25 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Deprecated // see "package-info" in generated folder
public class NamespacePrefixMapper extends com.sun.xml.bind.marshaller.NamespacePrefixMapper{
    private static final String ERP_URI = "urn://ru.gov.proc.erp.communication/2.0.5";
    private static final String ERP_REQUEST_TYPES_URI = "urn://ru.gov.proc.erp.communication/types/2.0.5";
    private static final String ETP_URI = "urn://ru.mos.etp.smev.erp/1.0.0";

    private static final String ERP_NMS = "erp";
    private static final String ERP_REQUEST_TYPES_NMS = "erp_types";
    private static final String ETP_NMS="etp";






    @Override
    public String getPreferredPrefix(final String namespaceUri, final String suggestion, final boolean requirePrefix) {
        if(ERP_URI.equals(namespaceUri)) {
            return ERP_NMS;
        } else if(ERP_REQUEST_TYPES_URI.equals(namespaceUri)) {
            return ERP_REQUEST_TYPES_NMS;
        }  else if(ETP_URI.equals(namespaceUri)) {
            return ETP_NMS;
        }
        return requirePrefix ? suggestion : StringUtils.EMPTY;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[] { ERP_REQUEST_TYPES_URI, ERP_URI, ETP_URI };
    }
}
