package ru.cip.ws.erp.servlet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;
import ru.cip.ws.erp.jpa.dao.PlanDaoImpl;
import ru.cip.ws.erp.jpa.dao.PlanErpDaoImpl;
import ru.cip.ws.erp.jpa.dao.PlanRecordDaoImpl;
import ru.cip.ws.erp.jpa.dao.PlanRecordErpDaoImpl;
import ru.cip.ws.erp.jpa.entity.PlanErp;
import ru.cip.ws.erp.jpa.entity.views.Plan;

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
    private PlanDaoImpl planViewDao;

    @Autowired
    private PlanErpDaoImpl planDao;

    @Autowired
    private PlanRecordDaoImpl planRecordViewDao;

    @Autowired
    private PlanRecordErpDaoImpl planRecordDao;


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
        processPlanCheckCancel(requestId, data_kind, request, response);
        logger.info("{} : End.", requestId);
    }

    private void processPlanCheckCancel(
            final String requestId,
            final DataKindEnum dataKind,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws IOException {
        final Integer param_check_plan_id = getIntegerParameter(request, PARAM_NAME_CHECK_PLAN_ID);
        logger.info("{} : is processPlanCheckCancel param ({}={})", requestId, PARAM_NAME_CHECK_PLAN_ID, param_check_plan_id);
        if (param_check_plan_id != null) {
            final Plan checkPlan = planViewDao.getById(param_check_plan_id);
            if (checkPlan == null) {
                logger.warn("{} : End. CheckPlan[{}] not found ", requestId, param_check_plan_id);
                response.getWriter().print(String.format("Не найден план проверки [%s]", param_check_plan_id));
                response.setStatus(500);
                return;
            }
            final List<PlanErp> activeByPlan = planDao.getActiveByPlan(checkPlan, dataKind);
            if (activeByPlan.isEmpty()) {
                logger.warn("{} : End. No active plan found ", requestId);
                response.getWriter().print("Нет сессий, доступных для прерывания");
                response.setStatus(500);
                return;
            }
            for (PlanErp planErp : activeByPlan) {
                planDao.cancel(planErp);
                logger.info("{} : : Canceled {}", requestId, planErp);
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
