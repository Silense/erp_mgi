package ru.cip.ws.erp.servlet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.cip.ws.erp.jpa.dao.CheckErpDaoImpl;
import ru.cip.ws.erp.jpa.dao.PlanDaoImpl;
import ru.cip.ws.erp.jpa.entity.views.Plan;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static ru.cip.ws.erp.servlet.ParameterNames.PARAM_DATA_KIND;
import static ru.cip.ws.erp.servlet.ParameterNames.PARAM_PLAN_ID;

/**
 * Author: Upatov Egor <br>
 * Date: 30.09.2016, 2:24 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Controller
public class stopErpUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(stopErpUpdateService.class);

    @Autowired
    private PlanDaoImpl planViewDao;

    @Autowired
    private CheckErpDaoImpl checkErpDao;

    @RequestMapping("/update/stop")
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String requestId = UUID.randomUUID().toString();
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        final String param_data_kind = getStringParameter(request, PARAM_DATA_KIND);
        logger.info("{} : Start handleRequest({}=\'{}\')", requestId, PARAM_DATA_KIND, param_data_kind);
        final DataKindEnum data_kind = DataKindEnum.getEnum(param_data_kind);
        if (data_kind == null) {
            logger.error("{} : End. Unknown {} = \'{}\' parameter value. Skip processing", requestId, PARAM_DATA_KIND, param_data_kind);
            response.setStatus(500);
            response.getWriter().println(String.format("Параметер %s имеет неизвестное значение \'%s\'", PARAM_DATA_KIND, param_data_kind));
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
        final Integer param_plan_id = getIntegerParameter(request, PARAM_PLAN_ID);
        logger.info("{} : is processPlanCheckCancel param ({}={})", requestId, PARAM_PLAN_ID, param_plan_id);
        if (param_plan_id != null) {
            final Plan plan = planViewDao.getById(param_plan_id);
            if (plan == null) {
                logger.warn("{} : End. Plan[{}] not found ", requestId, param_plan_id);
                response.getWriter().print(String.format("Не найден план проверки [%s]", param_plan_id));
                response.setStatus(500);
                return;
            }
            /*
            final List<PlanErp> activeByPlan = checkErpDao.getActiveByPlan(plan, dataKind);
            if (activeByPlan.isEmpty()) {
                logger.warn("{} : End. No active plan found ", requestId);
                response.getWriter().print("Нет сессий, доступных для прерывания");
                response.setStatus(500);
                return;
            }
            for (PlanErp planErp : activeByPlan) {
                checkErpDao.cancel(planErp);
                logger.info("{} : : Canceled {}", requestId, planErp);
            }
            */
            response.getWriter().print("Сессия завершена по запросу пользователя");
            response.setStatus(200);
        } else {
            logger.warn("{} : End. Not '{}' set", requestId, PARAM_PLAN_ID);
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
