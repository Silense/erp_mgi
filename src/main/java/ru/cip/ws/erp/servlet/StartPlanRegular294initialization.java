package ru.cip.ws.erp.servlet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;
import ru.cip.ws.erp.factory.JAXBMarshallerUtil;
import ru.cip.ws.erp.factory.XMLFactory;
import ru.cip.ws.erp.generated.erptypes.MessageToERP294Type;
import ru.cip.ws.erp.generated.erptypes.RequestMsg;
import ru.cip.ws.erp.jdbc.dao.CheckPlanDaoImpl;
import ru.cip.ws.erp.jdbc.dao.CheckPlanRecordDaoImpl;
import ru.cip.ws.erp.jdbc.dao.ExportSessionDaoImpl;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlan;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlanRecord;
import ru.cip.ws.erp.jdbc.entity.ExpSession;
import ru.cip.ws.erp.jdbc.entity.ExpSessionEvent;
import ru.cip.ws.erp.jms.MQMessageSender;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
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
    @Autowired
    private CheckPlanDaoImpl checkPlanDao;
    @Autowired
    private ExportSessionDaoImpl exportSessionDao;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final int requestNumber = counter.incrementAndGet();
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        logger.info("#{} Call StartErpUpdateServlet", requestNumber);
        final Integer param_check_plan_id = getIntegerParameter(request, PARAM_NAME_CHECK_PLAN_ID);
        final Integer param_year = getIntegerParameter(request, PARAM_NAME_YEAR);
        final String param_accepted_name = getStringParameter(request, PARAM_NAME_ACCEPTED_NAME);
        logger.info(
                "#{} Parsed params ({}='{}', {}='{}', {}='{}')",
                requestNumber,
                PARAM_NAME_CHECK_PLAN_ID,
                param_check_plan_id,
                PARAM_NAME_YEAR,
                param_year,
                PARAM_NAME_ACCEPTED_NAME,
                param_accepted_name
        );
        if (param_year == null && param_check_plan_id == null) {
            logger.warn("#{} End. Not '{}' or '{}' set", requestNumber, PARAM_NAME_YEAR, PARAM_NAME_CHECK_PLAN_ID);
            response.getWriter().print("Не указан ни идентифкатор плана проверки, ни год (один из параметров должен быть указан, год приоритетнее)");
            response.setStatus(400);
            return;
        }
        final CipCheckPlan checkPlan = (param_year != null) ? checkPlanDao.getByYear(param_year) : checkPlanDao.getById(param_check_plan_id);
        if (checkPlan == null) {
            logger.warn("#{} End. CheckPlan not found", requestNumber);
            response.getWriter().print("Не найден план проверки " + (param_year != null ? "(по году)" : "(по идентификатору)"));
            response.setStatus(404);
            return;
        } else {
            logger.debug("#{} founded CheckPlan: {}", requestNumber, checkPlan);
        }
        final List<CipCheckPlanRecord> checkPlanRecords = checkPlanRecordDao.getRecordsByPlanId(checkPlan.getCHECK_PLAN_ID());
        if (checkPlanRecords.isEmpty()) {
            logger.warn("#{} End. Not found any PlanRecords by check_plan_id = {}", requestNumber, checkPlan.getCHECK_PLAN_ID());
            response.getWriter().println(String.format("По плану проверок [%d] не найдено проверок", checkPlan.getCHECK_PLAN_ID()));
            response.setStatus(404);
            response.flushBuffer();
            return;
        } else if (logger.isDebugEnabled()) {
            for (CipCheckPlanRecord checkPlanRecord : checkPlanRecords) {
                logger.debug("#{} Founded record: {}", requestNumber, checkPlanRecord);
            }
        }
        final UUID requestId = UUID.randomUUID();
        final JAXBElement<RequestMsg> jaxb_messsage = messageFactory.constructPlanRegular294initialization(
                StringUtils.defaultString(param_accepted_name, checkPlan.getACCEPTED_NAME()),
                param_year != null ? param_year : checkPlan.getYEAR(),
                checkPlanRecords,
                requestId
        );
        try {
            final String result = JAXBMarshallerUtil.marshalAsString(jaxb_messsage);
            if (!StringUtils.isEmpty(result)) {
                final ExpSession exportSession = exportSessionDao.createNewExportSession(
                        "2.2.2 Первичное размещение плана плановых проверок",
                        MessageToERP294Type.PlanRegular294Initialization.class.getSimpleName(),
                        requestId.toString()
                );
                logger.info("#{} Create new ExportSession: {}", requestNumber, exportSession);
                final ExpSessionEvent exportEvent = exportSessionDao.createNewExportEvent(result, exportSession);
                logger.info("#{} Create new ExportEvent: {}", requestNumber, exportEvent);
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

    private String getStringParameter(final HttpServletRequest request, final String parameterName) {
        return request.getParameter(parameterName);
    }

    private Integer getIntegerParameter(final HttpServletRequest request, final String parameterName) {
        final String parameterValueStr = request.getParameter(parameterName);
        if (StringUtils.isNotEmpty(parameterValueStr)) {
            try {
                return Integer.valueOf(parameterValueStr);
            } catch (NumberFormatException e) {
                logger.warn("Cannot cast parameter \'{}\' with value \'{}\' to integer", parameterName, parameterValueStr);
            }
        }
        logger.warn("Parameter \'{}\' is not set", parameterName);
        return null;

    }
}
