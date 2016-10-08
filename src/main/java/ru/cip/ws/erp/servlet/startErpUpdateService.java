package ru.cip.ws.erp.servlet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;
import ru.cip.ws.erp.business.MessageProcessor;
import ru.cip.ws.erp.business.TestMessageProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

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
    private static final String PARAM_NAME_CHECK_PLAN_ID = "CHECK_PLAN_ID";
    private static final String PARAM_NAME_YEAR = "YEAR";
    private static final String PARAM_NAME_ACCEPTED_NAME = "ACCEPTED_NAME";
    private static final String PARAM_NAME_CHECK_PLAN_ERP_ID = "ID_CHECK_PLAN_ERP";

    @Autowired
    private MessageProcessor messageProcessor;
    @Autowired
    private TestMessageProcessor testMessageProcessor;


    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String requestId = UUID.randomUUID().toString();
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        final String param_data_kind = getStringParameter(request, PARAM_NAME_DATA_KIND);
        final boolean isTestMode = StringUtils.equalsIgnoreCase("TRUE", getStringParameter(request, PARAM_NAME_TEST));
        logger.info("{} : Start handleRequest({}=\'{}\')", requestId, PARAM_NAME_DATA_KIND, param_data_kind);
        if (isTestMode) {
            logger.warn("{} : IN TEST_MODE WE MUST SEND REFERENCE EXAMPLE MESSAGE", requestId);
        }
        final DataKindEnum data_kind = DataKindEnum.getEnum(param_data_kind);
        if (data_kind == null) {
            logger.error("{} : End. Unknown {} = \'{}\' parameter value. Skip processing", requestId, PARAM_NAME_DATA_KIND, param_data_kind);
            response.getWriter().println(String.format("Параметер %s имеет неизвестное значение \'%s\'", PARAM_NAME_DATA_KIND, param_data_kind));
            response.setStatus(500);
            return;
        }
        switch (data_kind) {
            case PROSECUTOR_ACK:
                processProsecutorAsk(request, response, requestId, isTestMode);
                break;
            case PLAN_REGULAR_294_INITIALIZATION:
                processPlanRegular294Initialization(request, response, requestId, isTestMode);
                break;
            case PLAN_REGULAR_294_CORRECTION:
                processPlanRegular294Correction(request, response, requestId, isTestMode);
                break;
            case PLAN_RESULT_294_INITIALIZATION:
                processPlanResult294Initialization(request, response, requestId, isTestMode);
                break;
            case PLAN_RESULT_294_CORRECTION:
                processPlanResult294Correction(request, response, requestId, isTestMode);
                break;
            case UPLAN_UNREGULAR_294_INITIALIZATION:
                processUplanUnRegular294Initialization(request, response, requestId, isTestMode);
                break;
            case UPLAN_UNREGULAR_294_CORRECTION:
                processUplanUnRegular294Correction(request, response, requestId, isTestMode);
                break;
            case UPLAN_RESULT_294_INITIALIZATION:
                processUplanResult294Initialization(request, response, requestId, isTestMode);
                break;
            case UPLAN_RESULT_294_CORRECTION:
                processUplanResult294Correction(request, response, requestId, isTestMode);
                break;
            default:
                logger.error("{} : End. Unknown {} = \'{}\' parameter value. Skip processing", requestId, PARAM_NAME_DATA_KIND, param_data_kind);
                response.getWriter().println(String.format("Параметер %s имеет неизвестное значение \'%s\'", PARAM_NAME_DATA_KIND, param_data_kind));
                response.setStatus(400);
                break;
        }
        logger.info("{} : End.", requestId);
    }

    private void processUplanResult294Correction(
            final HttpServletRequest request, final HttpServletResponse response, final String requestId, final boolean isTestMode
    ) throws IOException {
        if (isTestMode) {
            testMessageProcessor.processUplanResult294Correction(requestId, response);
            return;
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processUplanResult294Initialization(
            final HttpServletRequest request, final HttpServletResponse response, final String requestId, final boolean isTestMode
    ) throws IOException {
        if (isTestMode) {
            testMessageProcessor.processUplanResult294Initialization(requestId, response);
            return;
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processUplanUnRegular294Correction(
            final HttpServletRequest request, final HttpServletResponse response, final String requestId, final boolean isTestMode
    ) throws IOException {
        if (isTestMode) {
            testMessageProcessor.processUplanUnRegular294Correction(requestId, response);
            return;
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processUplanUnRegular294Initialization(
            final HttpServletRequest request, final HttpServletResponse response, final String requestId, final boolean isTestMode
    ) throws IOException {
        if (isTestMode) {
            testMessageProcessor.processUplanUnRegular294Initialization(requestId, response);
            return;
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processPlanResult294Correction(
            final HttpServletRequest request, final HttpServletResponse response, final String requestId, final boolean isTestMode
    ) throws IOException {
        if (isTestMode) {
            testMessageProcessor.processPlanResult294Correction(requestId, response);
            return;
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processPlanResult294Initialization(
            final HttpServletRequest request, final HttpServletResponse response, final String requestId, final boolean isTestMode
    ) throws IOException {
        if (isTestMode) {
            testMessageProcessor.processPlanResult294Initialization(requestId, response);
            return;
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processPlanRegular294Correction(
            final HttpServletRequest request, final HttpServletResponse response, final String requestId, final boolean isTestMode
    ) throws IOException {
        if (isTestMode) {
            testMessageProcessor.processPlanRegular294Correction(requestId, response);
            return;
        }
        final Integer param_check_plan_id = getIntegerParameter(request, PARAM_NAME_CHECK_PLAN_ID);
        final Integer param_year = getIntegerParameter(request, PARAM_NAME_YEAR);
        final String param_accepted_name = getStringParameter(request, PARAM_NAME_ACCEPTED_NAME);
        logger.info(
                "{} : parsed params ({}='{}', {}='{}', {}='{}')",
                requestId,
                PARAM_NAME_CHECK_PLAN_ID,
                param_check_plan_id,
                PARAM_NAME_YEAR,
                param_year,
                PARAM_NAME_ACCEPTED_NAME,
                param_accepted_name
        );
        if (param_check_plan_id == null) {
            logger.warn("{} : End. Not '{}' set", requestId, PARAM_NAME_CHECK_PLAN_ID);
            response.getWriter().print("Не указан идентифкатор плана проверки");
            response.setStatus(500);
        } else {
            messageProcessor.processPlanRegular294Correction(
                    requestId, response, param_check_plan_id, param_year, param_accepted_name
            );
        }
    }


    private void processPlanRegular294Initialization(
            final HttpServletRequest request, final HttpServletResponse response, final String requestId, final boolean isTestMode
    ) throws IOException {
        if (isTestMode) {
            testMessageProcessor.processPlanRegular294Initialization(requestId, response);
            return;
        }
        final Integer param_check_plan_id = getIntegerParameter(request, PARAM_NAME_CHECK_PLAN_ID);
        final Integer param_year = getIntegerParameter(request, PARAM_NAME_YEAR);
        final String param_accepted_name = getStringParameter(request, PARAM_NAME_ACCEPTED_NAME);
        logger.info(
                "{} : parsed params ({}='{}', {}='{}', {}='{}')",
                requestId,
                PARAM_NAME_CHECK_PLAN_ID,
                param_check_plan_id,
                PARAM_NAME_YEAR,
                param_year,
                PARAM_NAME_ACCEPTED_NAME,
                param_accepted_name
        );
        if (param_check_plan_id == null) {
            logger.warn("{} : End. Not '{}' set", requestId, PARAM_NAME_CHECK_PLAN_ID);
            response.getWriter().print("Не указан идентифкатор плана проверки");
            response.setStatus(500);
        } else {
            messageProcessor.processPlanRegular294Initialization(
                    requestId, response, param_check_plan_id, param_year, param_accepted_name
            );
        }
    }

    private void processProsecutorAsk(
            final HttpServletRequest request, final HttpServletResponse response, final String requestId, final boolean isTestMode
    ) throws IOException {
        logger.info("{} : is ProsecutorAsk", requestId);
        if (isTestMode) {
            logger.warn("{} : IN TEST_MODE WE MUST SEND REFERENCE EXAMPLE MESSAGE", requestId);
            testMessageProcessor.processProsecutorAsk(requestId, response);
            return;
        }
        messageProcessor.processProsecutorAsk(response, requestId);
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
        return null;
    }
}
