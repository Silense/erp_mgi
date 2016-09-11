package ru.cip.ws.erp.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;
import ru.cip.ws.erp.jms.MQMessageSender;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Upatov Egor <br>
 * Date: 10.09.2016, 16:10 <br>
 * Description:
 */

@Component
public class StartErpUpdateServlet implements HttpRequestHandler {

    @Autowired
    private MQMessageSender mqMessageSender;

    final static Logger logger = LoggerFactory.getLogger(StartErpUpdateServlet.class);
    final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final int requestNumber = counter.incrementAndGet();
        logger.info("#{} Call StartErpUpdateServlet", requestNumber);

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        String result = "Hello world";
        try {
            mqMessageSender.send(result);
        } catch (JMSException e) {
            logger.error("#{} Error : ", requestNumber, e);
        }
        response.getWriter().println(result);
        logger.info("#{} End of StartErpUpdateServlet", requestNumber);
    }
}
