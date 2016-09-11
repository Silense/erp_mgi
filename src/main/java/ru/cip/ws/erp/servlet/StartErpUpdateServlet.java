package ru.cip.ws.erp.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestHandler;
import ru.cip.ws.erp.factory.JAXBMarshallerUtil;
import ru.cip.ws.erp.factory.XMLFactory;
import ru.cip.ws.erp.generated.erptypes.LetterToERPType;
import ru.cip.ws.erp.jms.MQMessageSender;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Upatov Egor <br>
 * Date: 10.09.2016, 16:10 <br>
 * Description:
 */

@Component
public class StartErpUpdateServlet implements HttpRequestHandler {

    final static Logger logger = LoggerFactory.getLogger(StartErpUpdateServlet.class);
    final AtomicInteger counter = new AtomicInteger(0);
    @Autowired
    private MQMessageSender mqMessageSender;

    @Autowired
    private XMLFactory messageFactory;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final int requestNumber = counter.incrementAndGet();
        logger.info("#{} Call StartErpUpdateServlet", requestNumber);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        final JAXBElement<LetterToERPType> letterToERPTypeJAXBElement = messageFactory.constructPlanRegular294initialization();
        try {
            final String result = JAXBMarshallerUtil.marshalAsString(letterToERPTypeJAXBElement);
            if (!StringUtils.isEmpty(result)) {
                final String messageId = mqMessageSender.send(result);
                response.getWriter().println(result);
                response.setStatus(200);
                response.setHeader("MessageID", messageId);
                response.flushBuffer();
            }
        } catch (JMSException e) {
            logger.error("#{} Error in JMS session  : ", requestNumber, e);
            response.setStatus(500);
            response.getWriter().println("Ошибка в общении с сервисом MQ");
        } catch (JAXBException e) {
            logger.error("#{} Error while marshal as String : ", requestNumber, e);
            response.setStatus(500);
            response.getWriter().println("Ошибка при формировании сообщения");
        }
        logger.info("#{} End of StartErpUpdateServlet", requestNumber);
    }
}
