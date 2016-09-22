package ru.cip.ws.erp.servlet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;
import ru.cip.ws.erp.business.MessageService;
import ru.cip.ws.erp.business.TestMessageService;
import ru.cip.ws.erp.jdbc.dao.CheckPlanDaoImpl;
import ru.cip.ws.erp.jdbc.dao.CheckPlanRecordDaoImpl;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlan;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlanRecord;
import ru.cip.ws.erp.jdbc.entity.PlanCheckErp;
import ru.cip.ws.erp.jdbc.entity.PlanCheckRecErp;

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

    private static final Logger logger = LoggerFactory.getLogger(startErpUpdateService.class);
    private static final String PARAM_NAME_TEST = "TEST";
    private static final String PARAM_NAME_DATA_KIND = "DATA_KIND";

    private static final String DATA_KIND_PROSECUTOR_ACK = "PROSECUTOR_ACK";
    private static final String DATA_KIND_PLAN_REGULAR_294_INITIALIZATION = "PLAN_REGULAR_294_INITIALIZATION";
    private static final String DATA_KIND_PLAN_REGULAR_294_CORRECTION = "PLAN_REGULAR_294_CORRECTION";
    private static final String DATA_KIND_PLAN_RESULT_294_INITIALIZATION = "PLAN_RESULT_294_INITIALIZATION";
    private static final String DATA_KIND_PLAN_RESULT_294_CORRECTION = "PLAN_RESULT_294_CORRECTION";
    private static final String DATA_KIND_UPLAN_UNREGULAR_294_INITIALIZATION = "UPLAN_UNREGULAR_294_INITIALIZATION";
    private static final String DATA_KIND_UPLAN_UNREGULAR_294_CORRECTION = "UPLAN_UNREGULAR_294_CORRECTION";
    private static final String DATA_KIND_UPLAN_RESULT_294_INITIALIZATION = "UPLAN_RESULT_294_INITIALIZATION";
    private static final String DATA_KIND_UPLAN_RESULT_294_CORRECTION = "UPLAN_RESULT_294_CORRECTION";


    private static final String PARAM_NAME_CHECK_PLAN_ID = "CHECK_PLAN_ID";
    private static final String PARAM_NAME_YEAR = "YEAR";
    private static final String PARAM_NAME_ACCEPTED_NAME = "ACCEPTED_NAME";

    private final AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    private MessageService messageService;
    @Autowired
    private TestMessageService testMessageService;
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
        final boolean isTest = StringUtils.equalsIgnoreCase(getStringParameter(request, PARAM_NAME_TEST), "true");
        if(!isTest) {
            logger.info("#{} Call StartErpUpdateServlet. {}=\'{}\'", requestNumber, PARAM_NAME_DATA_KIND, param_data_kind);
            if (DATA_KIND_PROSECUTOR_ACK.equalsIgnoreCase(param_data_kind)) {
                processProsecutorAsk(request, response, requestNumber);
            } else if (DATA_KIND_PLAN_REGULAR_294_INITIALIZATION.equalsIgnoreCase(param_data_kind)) {
                processPlanRegular294Initialization(request, response, requestNumber);
            } else if (DATA_KIND_PLAN_REGULAR_294_CORRECTION.equalsIgnoreCase(param_data_kind)) {
                processPlanRegular294Correction(request, response, requestNumber);
            } else if (DATA_KIND_PLAN_RESULT_294_INITIALIZATION.equalsIgnoreCase(param_data_kind)) {
                processPlanResult294Initialization(request, response, requestNumber);
            } else if (DATA_KIND_PLAN_RESULT_294_CORRECTION.equalsIgnoreCase(param_data_kind)) {
                processPlanResult294Correction(request, response, requestNumber);
            } else if (DATA_KIND_UPLAN_UNREGULAR_294_INITIALIZATION.equalsIgnoreCase(param_data_kind)) {
                processUplanUnRegular294Initialization(request, response, requestNumber);
            } else if (DATA_KIND_UPLAN_UNREGULAR_294_CORRECTION.equalsIgnoreCase(param_data_kind)) {
                processUplanUnRegular294Correction(request, response, requestNumber);
            } else if (DATA_KIND_UPLAN_RESULT_294_INITIALIZATION.equalsIgnoreCase(param_data_kind)) {
                processUplanResult294Initialization(request, response, requestNumber);
            } else if (DATA_KIND_UPLAN_RESULT_294_CORRECTION.equalsIgnoreCase(param_data_kind)) {
                processUplanResult294Correction(request, response, requestNumber);
            }
        } else {
            logger.info("#{} Call StartErpUpdateServlet IN TEST MODE. {}=\'{}\'", requestNumber, PARAM_NAME_DATA_KIND, param_data_kind);
            if (DATA_KIND_PROSECUTOR_ACK.equalsIgnoreCase(param_data_kind)) {
                testMessageService.processProsecutorAsk(requestNumber);
            } else if (DATA_KIND_PLAN_REGULAR_294_INITIALIZATION.equalsIgnoreCase(param_data_kind)) {
                testMessageService.processPlanRegular294Initialization(requestNumber);
            } else if (DATA_KIND_PLAN_REGULAR_294_CORRECTION.equalsIgnoreCase(param_data_kind)) {
                testMessageService.processPlanRegular294Correction(requestNumber);
            } else if (DATA_KIND_PLAN_RESULT_294_INITIALIZATION.equalsIgnoreCase(param_data_kind)) {
                testMessageService.processPlanResult294Initialization(requestNumber);
            } else if (DATA_KIND_PLAN_RESULT_294_CORRECTION.equalsIgnoreCase(param_data_kind)) {
                testMessageService.processPlanResult294Correction(requestNumber);
            } else if (DATA_KIND_UPLAN_UNREGULAR_294_INITIALIZATION.equalsIgnoreCase(param_data_kind)) {
                testMessageService.processUplanUnRegular294Initialization(requestNumber);
            } else if (DATA_KIND_UPLAN_UNREGULAR_294_CORRECTION.equalsIgnoreCase(param_data_kind)) {
                testMessageService.processUplanUnRegular294Correction(requestNumber);
            } else if (DATA_KIND_UPLAN_RESULT_294_INITIALIZATION.equalsIgnoreCase(param_data_kind)) {
                testMessageService.processUplanResult294Initialization(requestNumber);
            } else if (DATA_KIND_UPLAN_RESULT_294_CORRECTION.equalsIgnoreCase(param_data_kind)) {
                testMessageService.processUplanResult294Correction(requestNumber);
            }
        }
        logger.info("#{} End of StartErpUpdateServlet", requestNumber);
    }

    private void processUplanResult294Correction(
            final HttpServletRequest request, final HttpServletResponse response, final int requestNumber
    ) {
        //TODO
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processUplanResult294Initialization(
            final HttpServletRequest request, final HttpServletResponse response, final int requestNumber
    ) {
        //TODO
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processUplanUnRegular294Correction(
            final HttpServletRequest request, final HttpServletResponse response, final int requestNumber
    ) {
        //TODO
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processUplanUnRegular294Initialization(
            final HttpServletRequest request, final HttpServletResponse response, final int requestNumber
    ) {
        //TODO
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processPlanResult294Correction(
            final HttpServletRequest request, final HttpServletResponse response, final int requestNumber
    ) {
        //TODO
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processPlanResult294Initialization(
            final HttpServletRequest request, final HttpServletResponse response, final int requestNumber
    ) {
        //TODO
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processPlanRegular294Correction(
            final HttpServletRequest request, final HttpServletResponse response, final int requestNumber
    )
            throws IOException {
        final Integer param_check_plan_id = getIntegerParameter(request, PARAM_NAME_CHECK_PLAN_ID);
        final Integer param_year = getIntegerParameter(request, PARAM_NAME_YEAR);
        final String param_accepted_name = getStringParameter(request, PARAM_NAME_ACCEPTED_NAME);
        logger.info(
                "#{} PlanRegular294Correction : parsed params ({}='{}', {}='{}', {}='{}')",
                requestNumber,
                PARAM_NAME_CHECK_PLAN_ID,
                param_check_plan_id,
                PARAM_NAME_YEAR,
                param_year,
                PARAM_NAME_ACCEPTED_NAME,
                param_accepted_name
        );
        if (param_check_plan_id == null) {
            logger.warn("#{} End. Not '{}' set", requestNumber, PARAM_NAME_YEAR, PARAM_NAME_CHECK_PLAN_ID);
            response.getWriter().print("Не указан идентифкатор плана проверки");
            response.setStatus(400);
            return;
        }
        final CipCheckPlan checkPlan = checkPlanDao.getByIdFromView(param_check_plan_id);
        if (checkPlan == null) {
            logger.warn("#{} End. CheckPlan not found", requestNumber);
            response.getWriter().print("Не найден план проверки  (по идентификатору)");
            response.setStatus(404);
            return;
        } else {
            logger.debug("#{} founded CheckPlan: {}", requestNumber, checkPlan);
        }
        final PlanCheckErp planCheckErp = checkPlanDao.getById(checkPlan.getCHECK_PLAN_ID());
        if (planCheckErp == null) {
            logger.warn("#{} End. PLAN[{}] is not send for ERP", requestNumber, checkPlan.getCHECK_PLAN_ID());
            response.getWriter().print(
                    String.format("Нельзя корректировать план: План %d еще не был первично выгружен в ЕРП", checkPlan.getCHECK_PLAN_ID())
            );
            response.setStatus(400);
            return;
        } else {
            logger.debug("#{} founded PlanCheckErp: {}", requestNumber, planCheckErp);
        }
        if (planCheckErp.getCodeCheckPlanErp() == null) {
            logger.warn("#{} End. PLAN[{}] is not send for ERP", requestNumber, checkPlan.getCHECK_PLAN_ID());
            response.getWriter().print(
                    String.format(
                            "Нельзя корректировать план: По первичному размещению плана %d еще не было ответа из ЕРП",
                            checkPlan.getCHECK_PLAN_ID()
                    )
            );
            response.setStatus(400);
            return;
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
        final List<PlanCheckRecErp> sentCheckPlanRecords = checkPlanRecordDao.getRecordsByPlanId(planCheckErp.getIdCheckPlanErp());
        final String result = messageService.sendPlanRegular294Correction(
                checkPlan,
                checkPlanRecords,
                planCheckErp,
                sentCheckPlanRecords,
                param_accepted_name,
                param_year
        );
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


    private void processPlanRegular294Initialization(
            final HttpServletRequest request, final HttpServletResponse response, final int requestNumber
    )
            throws IOException {
        final Integer param_check_plan_id = getIntegerParameter(request, PARAM_NAME_CHECK_PLAN_ID);
        final Integer param_year = getIntegerParameter(request, PARAM_NAME_YEAR);
        final String param_accepted_name = getStringParameter(request, PARAM_NAME_ACCEPTED_NAME);
        logger.info(
                "#{} PlanRegular294Initialization : parsed params ({}='{}', {}='{}', {}='{}')",
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
        final CipCheckPlan checkPlan = (param_year != null) ? checkPlanDao.getByYearFromView(param_year) : checkPlanDao.getByIdFromView(
                param_check_plan_id
        );
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
        final String result = messageService.sendPlanRegular294Initialization(checkPlan, param_accepted_name, param_year, checkPlanRecords);
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

    private void processProsecutorAsk(
            final HttpServletRequest request, final HttpServletResponse response, final int requestNumber
    )
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
