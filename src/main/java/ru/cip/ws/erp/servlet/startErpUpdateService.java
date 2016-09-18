package ru.cip.ws.erp.servlet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;
import ru.cip.ws.erp.business.MessageService;
import ru.cip.ws.erp.jdbc.dao.CheckPlanDaoImpl;
import ru.cip.ws.erp.jdbc.dao.CheckPlanRecordDaoImpl;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlan;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlanRecord;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: Upatov Egor <br>
 * Date: 10.09.2016, 16:10 <br>
 * Description:
 */

@Component
public class startErpUpdateService implements HttpRequestHandler {

    private final static Logger logger = LoggerFactory.getLogger(startErpUpdateService.class);
    private final static String PARAM_NAME_DATA_KIND = "DATA_KIND";

    private final static String DATA_KIND_PROSECUTOR_ACK = "PROSECUTOR_ACK";
    private static final String DATA_KIND_CHECK_PLAN = "CHECK_PLAN";

    private static final String PARAM_NAME_CHECK_PLAN_ID = "CHECK_PLAN_ID";
    private static final String PARAM_NAME_YEAR = "YEAR";
    private static final String PARAM_NAME_ACCEPTED_NAME = "ACCEPTED_NAME";

    private final AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    private MessageService messageService;
    @Autowired
    private CheckPlanRecordDaoImpl checkPlanRecordDao;
    @Autowired
    private CheckPlanDaoImpl checkPlanDao;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final int requestNumber = counter.incrementAndGet();
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        final String param_data_kind = getStringParameter(request, PARAM_NAME_DATA_KIND);
        logger.info("#{} Call StartErpUpdateServlet. {}=\'{}\'", requestNumber, PARAM_NAME_DATA_KIND);
        if (DATA_KIND_PROSECUTOR_ACK.equalsIgnoreCase(param_data_kind)) {
            processProsecutorAsk(request, response, requestNumber);
        } else if (DATA_KIND_CHECK_PLAN.equalsIgnoreCase(param_data_kind)) {
            processCheckPlan(request, response, requestNumber);
        }
        logger.info("#{} End of StartErpUpdateServlet", requestNumber);
    }


    private void processCheckPlan(final HttpServletRequest request, final HttpServletResponse response, final int requestNumber) throws IOException {
        final Integer param_check_plan_id = getIntegerParameter(request, PARAM_NAME_CHECK_PLAN_ID);
        final Integer param_year = getIntegerParameter(request, PARAM_NAME_YEAR);
        final String param_accepted_name = getStringParameter(request, PARAM_NAME_ACCEPTED_NAME);
        logger.info(
                "#{} CheckPlan : parsed params ({}='{}', {}='{}', {}='{}')",
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
        final CipCheckPlan checkPlan = (param_year != null) ?
                checkPlanDao.getByYearFromView(param_year) : checkPlanDao.getByIdFromView(param_check_plan_id);
        if (checkPlan == null) {
            logger.warn("#{} End. CheckPlan not found", requestNumber);
            response.getWriter().print("Не найден план проверки " + (param_year != null ? "(по году)" : "(по идентификатору)"));
            response.setStatus(404);
            return;
        } else {
            logger.debug("#{} founded CheckPlan: {}", requestNumber, checkPlan);
        }
        final List<CipCheckPlanRecord> checkPlanRecords = checkPlanRecordDao.getRecordsFromViewByPlanId(checkPlan.getCHECK_PLAN_ID());
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
        final String result = messageService.sendPlanRegular294initialization(checkPlan, param_accepted_name, param_year, checkPlanRecords);
        if (StringUtils.isNotEmpty(result)) {
            response.setContentType("text/xml");
            response.getWriter().println(result);
            response.setStatus(200);
        } else {
            response.setContentType("text/xml");
            response.getWriter().println("Ошибка");
            response.setStatus(500);
        }
    }

    private void processProsecutorAsk(final HttpServletRequest request, final HttpServletResponse response, final int requestNumber)
            throws IOException {
        logger.info("#{} ProsecutorAsk", requestNumber);
        final String result = messageService.sendProsecutorAck();
        if (StringUtils.isNotEmpty(result)) {
            response.setContentType("text/xml");
            response.getWriter().println(result);
            response.setStatus(200);
        } else {
            response.setContentType("text/xml");
            response.getWriter().println("Ошибка");
            response.setStatus(500);
        }
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
