package ru.cip.ws.erp.factory;

/**
 * Author: Upatov Egor <br>
 * Date: 11.09.2016, 13:25 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
public class NamespacePrefixMapper extends com.sun.xml.bind.marshaller.NamespacePrefixMapper{
    private static final String TNS_URI = "urn://ru.gov.proc.erp.communication/2.0.5";
    private static final String ERP_REQUEST_TYPES_URI = "urn://ru.gov.proc.erp.communication/types/2.0.5";

    private static final String TNS_NMS = "tns";
    private static final String ERP_REQUEST_TYPES_NMS = "erp_types";






    @Override
    public String getPreferredPrefix(final String namespaceUri, final String suggestion, final boolean requirePrefix) {
        if(TNS_URI.equals(namespaceUri)) {
            return TNS_NMS;
        } else if(ERP_REQUEST_TYPES_URI.equals(namespaceUri)) {
            return ERP_REQUEST_TYPES_NMS;
        }
        return suggestion;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[] { ERP_REQUEST_TYPES_URI, TNS_URI };
    }
}
