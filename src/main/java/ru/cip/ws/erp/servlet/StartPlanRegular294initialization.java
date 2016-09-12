package ru.cip.ws.erp.servlet;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestHandler;
import ru.cip.ws.erp.factory.JAXBMarshallerUtil;
import ru.cip.ws.erp.factory.XMLFactory;
import ru.cip.ws.erp.generated.erptypes.LetterToERPType;
import ru.cip.ws.erp.jdbc.dao.CheckPlanRecordDaoImpl;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlanRecord;
import ru.cip.ws.erp.jms.MQMessageSender;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Upatov Egor <br>
 * Date: 10.09.2016, 16:10 <br>
 * Description:
 */

@Component
public class StartPlanRegular294initialization implements HttpRequestHandler {

    private final static Logger logger = LoggerFactory.getLogger(StartPlanRegular294initialization.class);
    private static final String PARAM_NAME_CHECK_PLAN_ID = "CHECK_PLAN_ID";
    private static final String PARAM_NAME_YEAR = "YEAR";
    private static final String PARAM_NAME_ACCEPTED_NAME = "ACCEPTED_NAME";
    private final AtomicInteger counter = new AtomicInteger(0);
    @Autowired
    private MQMessageSender mqMessageSender;
    @Autowired
    private XMLFactory messageFactory;
    @Autowired
    private CheckPlanRecordDaoImpl checkPlanRecordDao;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final int requestNumber = counter.incrementAndGet();
        Integer param_check_plan_id_parsed;
        Integer param_year_parsed;
        final String param_check_plan_id = request.getParameter(PARAM_NAME_CHECK_PLAN_ID);
        final String param_year = request.getParameter(PARAM_NAME_YEAR);
        final String param_accepted_name = request.getParameter(PARAM_NAME_ACCEPTED_NAME);
        logger.info(
                "#{} Call StartErpUpdateServlet({}='{}', {}='{}', {}='{}')",
                requestNumber,
                PARAM_NAME_CHECK_PLAN_ID,
                param_check_plan_id,
                PARAM_NAME_YEAR,
                param_year,
                PARAM_NAME_ACCEPTED_NAME,
                param_accepted_name
        );
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        if (StringUtils.isEmpty(param_check_plan_id)) {
            logger.warn("#{} End. Parameter {} is empty", requestNumber, PARAM_NAME_CHECK_PLAN_ID);
            response.getWriter().println(
                    String.format("Необходимо указать в качестве параметра идентификатор плана проверки [%s]", PARAM_NAME_CHECK_PLAN_ID)
            );
            response.setStatus(400);
            response.flushBuffer();
            return;
        } else {
            param_check_plan_id_parsed = NumberUtils.createInteger(param_check_plan_id);
            if(param_check_plan_id_parsed == null){
                logger.warn("#{} End. Parameter {}[value='{}'] is not integer", requestNumber, PARAM_NAME_CHECK_PLAN_ID, param_check_plan_id);
                response.getWriter().println(
                        String.format("Параметр идентификатора плана проверки [%s] должен быть целочисленным", PARAM_NAME_CHECK_PLAN_ID)
                );
                response.setStatus(400);
                response.flushBuffer();
                return;
            }
        }
        if (StringUtils.isEmpty(param_year)) {
            logger.warn("#{} End. Parameter {} is empty", requestNumber, PARAM_NAME_YEAR);
            response.getWriter().println(String.format("Необходимо указать в качестве параметра год [%s]", PARAM_NAME_YEAR));
            response.setStatus(400);
            response.flushBuffer();
            return;
        } else {
            param_year_parsed = NumberUtils.createInteger(param_year);
            if( param_year_parsed == null){
                logger.warn("#{} End. Parameter {}[value='{}'] is not integer", requestNumber, PARAM_NAME_YEAR, param_year);
                response.getWriter().println(String.format("Параметр год [%s] должен быть целочисленным", PARAM_NAME_YEAR));
                response.setStatus(400);
                response.flushBuffer();
                return;
            }
        }

        final List<CipCheckPlanRecord> checkPlanRecords = checkPlanRecordDao.getRecordsByPlanId(param_check_plan_id_parsed);
        if(checkPlanRecords.isEmpty()){
            logger.warn("#{} End. Not found any PlanRecords by check_plan_id = {}", requestNumber, param_check_plan_id_parsed);
            response.getWriter().println(String.format("По идентифкатору плана проверок [%d] не найдено проверок", param_check_plan_id_parsed));
            response.setStatus(400);
            response.flushBuffer();
            return;
        }
        if (logger.isDebugEnabled()) {
            for (CipCheckPlanRecord checkPlanRecord : checkPlanRecords) {
                logger.debug("#{} Founded record: {}", requestNumber, checkPlanRecord);
            }
        }
        final JAXBElement<LetterToERPType> letterToERPTypeJAXBElement = messageFactory.constructPlanRegular294initialization(
                param_accepted_name, param_year_parsed, checkPlanRecords
        );
        try {
            final String result = JAXBMarshallerUtil.marshalAsString(letterToERPTypeJAXBElement);
            if (!StringUtils.isEmpty(result)) {
                final String messageId = mqMessageSender.send(result);
                response.setContentType("text/xml");
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
