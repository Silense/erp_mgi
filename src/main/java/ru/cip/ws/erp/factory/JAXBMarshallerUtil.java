package ru.cip.ws.erp.factory;

import ru.cip.ws.erp.generated.erptypes.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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
}
