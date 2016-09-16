package ru.cip.ws.erp.factory;

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
    public static String marshalAsString(Object item) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapper());
        final StringWriter sw = new StringWriter();
        marshaller.marshal(item, sw);
        return sw.toString();
    }

    public static ResponseMsg unmarshalResponse(final String raw) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final Source is = new StringSource(raw);
        final JAXBElement<ResponseMsg> root = unmarshaller.unmarshal(is, ResponseMsg.class);
        return root.getValue();
    }
}
