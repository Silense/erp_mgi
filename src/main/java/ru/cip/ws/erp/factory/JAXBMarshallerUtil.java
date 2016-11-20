package ru.cip.ws.erp.factory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.xml.transform.StringSource;
import ru.cip.ws.erp.generated.erptypes.ObjectFactory;
import ru.cip.ws.erp.generated.erptypes.ResponseMsg;

import javax.xml.bind.*;
import javax.xml.transform.Source;
import java.io.StringWriter;

/**
 * Author: Upatov Egor <br>
 * Date: 11.09.2016, 12:06 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
public class JAXBMarshallerUtil {

    private final static Logger logger = LoggerFactory.getLogger(JAXBMarshallerUtil.class);


    public static String marshalAsString(final Object item, final String requestId) {
        if (item == null) {
            logger.error("{} End. Error: Marshalling called with NULL as item", requestId);
            return null;
        }
        final StringWriter sw = new StringWriter();
        try {
            final JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
            final Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(item, sw);
            final String result = sw.toString();
            if (StringUtils.isEmpty(result)) {
                logger.error("{} End. Error: After marshalling result message is null or empty", requestId);
                return null;
            }
            return result;
        } catch (final JAXBException e) {
            logger.error("{} End. Error: Marshalling failed with error", requestId, e);
            return null;
        }
    }

    public static ResponseMsg unmarshalResponse(final String raw) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final Source is = new StringSource(raw);
        final JAXBElement<ResponseMsg> root = unmarshaller.unmarshal(is, ResponseMsg.class);
        return root.getValue();
    }
}
