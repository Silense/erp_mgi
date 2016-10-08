package ru.cip.ws.erp.servlet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;
import ru.cip.ws.erp.jdbc.dao.CheckPlanDaoImpl;
import ru.cip.ws.erp.jdbc.dao.CheckPlanRecordDaoImpl;
import ru.cip.ws.erp.jdbc.dao.PlanCheckErpDaoImpl;
import ru.cip.ws.erp.jdbc.dao.PlanCheckRecordErpDaoImpl;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlan;
import ru.cip.ws.erp.jdbc.entity.PlanCheckErp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Author: Upatov Egor <br>
 * Date: 30.09.2016, 2:24 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Component
public class stopErpUpdateService implements HttpRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(stopErpUpdateService.class);
    private static final String PARAM_NAME_DATA_KIND = "DATA_KIND";
    private static final String PARAM_NAME_CHECK_PLAN_ID = "CHECK_PLAN_ID";

    @Autowired
    private CheckPlanDaoImpl planViewDao;

    @Autowired
    private PlanCheckErpDaoImpl planDao;

    @Autowired
    private CheckPlanRecordDaoImpl planRecordViewDao;

    @Autowired
    private PlanCheckRecordErpDaoImpl planRecordDao;


    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String requestId = UUID.randomUUID().toString();
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        final String param_data_kind = getStringParameter(request, PARAM_NAME_DATA_KIND);
        logger.info("{} : Start handleRequest({}=\'{}\')", requestId, PARAM_NAME_DATA_KIND, param_data_kind);
        final DataKindEnum data_kind = DataKindEnum.getEnum(param_data_kind);
        if (data_kind == null) {
            logger.error("{} : End. Unknown {} = \'{}\' parameter value. Skip processing", requestId, PARAM_NAME_DATA_KIND, param_data_kind);
            response.setStatus(500);
            response.getWriter().println(String.format("Параметер %s имеет неизвестное значение \'%s\'", PARAM_NAME_DATA_KIND, param_data_kind));
            return;
        }
        switch (data_kind) {
            case PROSECUTOR_ACK:
                break;
            case PLAN_REGULAR_294_INITIALIZATION:
                processPlanRegular294Initialization(request, response, requestId);
                break;
            case PLAN_REGULAR_294_CORRECTION:
                //processPlanRegular294Correction(request, response, requestId, isTestMode);
                break;
            case PLAN_RESULT_294_INITIALIZATION:
                //processPlanResult294Initialization(request, response, requestId, isTestMode);
                break;
            case PLAN_RESULT_294_CORRECTION:
                //processPlanResult294Correction(request, response, requestId, isTestMode);
                break;
            case UPLAN_UNREGULAR_294_INITIALIZATION:
                // processUplanUnRegular294Initialization(request, response, requestId, isTestMode);
                break;
            case UPLAN_UNREGULAR_294_CORRECTION:
                // processUplanUnRegular294Correction(request, response, requestId, isTestMode);
                break;
            case UPLAN_RESULT_294_INITIALIZATION:
                // processUplanResult294Initialization(request, response, requestId, isTestMode);
                break;
            case UPLAN_RESULT_294_CORRECTION:
                // processUplanResult294Correction(request, response, requestId, isTestMode);
                break;
            default:
                logger.error("{} : End. Unknown {} = \'{}\' parameter value. Skip processing", requestId, PARAM_NAME_DATA_KIND, param_data_kind);
                response.setStatus(400);
                response.getWriter().println(String.format("Параметер %s имеет неизвестное значение \'%s\'", PARAM_NAME_DATA_KIND, param_data_kind));
                break;
        }
        logger.info("{} : End.", requestId);
    }

    private void processPlanRegular294Initialization(final HttpServletRequest request, final HttpServletResponse response, final String requestId)
            throws IOException {
        final Integer param_check_plan_id = getIntegerParameter(request, PARAM_NAME_CHECK_PLAN_ID);
        logger.info("{} : is PlanRegular294Initialization param ({}={})", requestId, PARAM_NAME_CHECK_PLAN_ID, param_check_plan_id);
        if(param_check_plan_id != null) {
            final CipCheckPlan checkPlan = planViewDao.getById(param_check_plan_id);
            if(checkPlan == null){
                logger.warn("{} : End. CheckPlan[{}] not found ", requestId, param_check_plan_id);
                response.getWriter().print(String.format("Не найден план проверки [%s]", param_check_plan_id));
                response.setStatus(500);
                return;
            }
            final List<PlanCheckErp> activeByPlan = planDao.getActiveByPlan(checkPlan);
            for (PlanCheckErp planCheckErp : activeByPlan) {
                planDao.cancel(planCheckErp);
                logger.info("{} : : Canceled {}", requestId, planCheckErp);
            }
            response.getWriter().print("Сессия завершена по запросу пользователя");
            response.setStatus(200);
        } else {
            logger.warn("{} : End. Not '{}' set", requestId, PARAM_NAME_CHECK_PLAN_ID);
            response.getWriter().print("Не указан идентифкатор плана проверки для отмены первичной выгрузки");
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
