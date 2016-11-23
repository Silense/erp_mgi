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

import static ru.cip.ws.erp.servlet.ParameterNames.*;

/**
 * Author: Upatov Egor <br>
 * Date: 10.09.2016, 16:10 <br>
 * Description:
 */

@Component
public class startErpUpdateService implements HttpRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(startErpUpdateService.class);


    @Autowired
    private MessageProcessor messageProcessor;
    @Autowired
    private TestMessageProcessor testMessageProcessor;


    @Override
    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String uuid = UUID.randomUUID().toString();
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        final String param_data_kind = getStringParameter(request, PARAM_DATA_KIND);
        logger.info("{} : Start handleRequest({}=\'{}\')", uuid, PARAM_DATA_KIND, param_data_kind);
        final DataKindEnum data_kind = DataKindEnum.getEnum(param_data_kind);
        if (data_kind == null) {
            logger.error("{} : End. Unknown {} = \'{}\' parameter value. Skip processing", uuid, PARAM_DATA_KIND, param_data_kind);
            response.getWriter().println(String.format("Параметер %s имеет неизвестное значение \'%s\'", PARAM_DATA_KIND, param_data_kind));
            response.setStatus(500);
            return;
        }
        final boolean isTestMode = StringUtils.equalsIgnoreCase("TRUE", getStringParameter(request, PARAM_TEST));
        if (isTestMode) {
            logger.warn("{} : IN TEST_MODE WE MUST SEND REFERENCE EXAMPLE MESSAGE", uuid);
        }
        switch (data_kind) {
            case PROSECUTOR_ACK:
                if (isTestMode) {
                    testMessageProcessor.processProsecutorAsk(uuid, response);
                } else {
                    processProsecutorAsk(request, response, uuid);
                }
                break;
            case PLAN_REGULAR_294_INITIALIZATION:
                if (isTestMode) {
                    testMessageProcessor.processPlanRegular294Initialization(uuid, response);
                } else {
                    processPlanRegular294Initialization(request, response, uuid);
                }
                break;
            case PLAN_REGULAR_294_CORRECTION:
                if (isTestMode) {
                    testMessageProcessor.processPlanRegular294Correction(uuid, response);
                } else {
                    processPlanRegular294Correction(request, response, uuid);
                }
                break;
            case PLAN_RESULT_294_INITIALIZATION:
                if (isTestMode) {
                    testMessageProcessor.processPlanResult294Initialization(uuid, response);
                } else {
                    processPlanResult294Initialization(request, response, uuid);
                }
                break;
            case PLAN_RESULT_294_CORRECTION:
                if (isTestMode) {
                    testMessageProcessor.processPlanResult294Correction(uuid, response);
                } else {
                    processPlanResult294Correction(request, response, uuid);
                }
                break;
            // Внеплановые проверки
            case UPLAN_UNREGULAR_294_INITIALIZATION:
                if (isTestMode) {
                    testMessageProcessor.processUplanUnRegular294Initialization(uuid, response);
                } else {
                    processUplanUnRegular294Initialization(request, response, uuid);
                }
                break;
            case UPLAN_UNREGULAR_294_CORRECTION:
                if (isTestMode) {
                    testMessageProcessor.processUplanUnRegular294Correction(uuid, response);
                } else {
                    processUplanUnRegular294Correction(request, response, uuid);
                }
                break;
            case UPLAN_RESULT_294_INITIALIZATION:
                if (isTestMode) {
                    testMessageProcessor.processUplanResult294Initialization(uuid, response);
                } else {
                    processUplanResult294Initialization(request, response, uuid);
                }
                break;
            case UPLAN_RESULT_294_CORRECTION:
                if (isTestMode) {
                    testMessageProcessor.processUplanResult294Correction(uuid, response);
                } else {
                    processUplanResult294Correction(request, response, uuid);
                }
                break;
            default:
                logger.error("{} : End. Unknown {} = \'{}\' parameter value. Skip processing", uuid, PARAM_DATA_KIND, param_data_kind);
                response.getWriter().println(String.format("Параметер %s имеет неизвестное значение \'%s\'", PARAM_DATA_KIND, param_data_kind));
                response.setStatus(400);
                break;
        }
        logger.info("{} : End.", uuid);
    }

    private void processUplanResult294Correction(final HttpServletRequest request, final HttpServletResponse response, final String uuid)
            throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processUplanResult294Initialization(final HttpServletRequest request, final HttpServletResponse response, final String uuid)
            throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processUplanUnRegular294Correction(final HttpServletRequest request, final HttpServletResponse response, final String uuid)
            throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processUplanUnRegular294Initialization(final HttpServletRequest request, final HttpServletResponse response, final String uuid)
            throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void processPlanResult294Correction(final HttpServletRequest request, final HttpServletResponse response, final String uuid)
            throws IOException {
        final Integer planId = getIntegerParameter(request, PARAM_PLAN_ID);
        final Integer year = getIntegerParameter(request, PARAM_YEAR);
        logger.info("{} : params ({}='{}', {}='{}', {}='{}')", uuid, PARAM_PLAN_ID, planId, PARAM_YEAR, year);
        if (planId == null) {
            logger.warn("{} : End. Not '{}' set", uuid, PARAM_PLAN_ID);
            response.getWriter().print("Не указан идентифкатор плана проверки");
            response.setStatus(500);
            return;
        }
        messageProcessor.processPlanResult294Correction(uuid, response, planId, year);
    }

    private void processPlanResult294Initialization(final HttpServletRequest request, final HttpServletResponse response, final String uuid)
            throws IOException {
        final Integer planId = getIntegerParameter(request, PARAM_PLAN_ID);
        final Integer year = getIntegerParameter(request, PARAM_YEAR);
        logger.info("{} : params ({}='{}', {}='{}', {}='{}')", uuid, PARAM_PLAN_ID, planId, PARAM_YEAR, year);
        if (planId == null) {
            logger.warn("{} : End. Not '{}' set", uuid, PARAM_PLAN_ID);
            response.getWriter().print("Не указан идентифкатор плана проверки");
            response.setStatus(500);
            return;
        }
        messageProcessor.processPlanResult294Initialization(uuid, response, planId, year);
    }

    private void processPlanRegular294Correction(final HttpServletRequest request, final HttpServletResponse response, final String uuid)
            throws IOException {
        final Integer planId = getIntegerParameter(request, PARAM_PLAN_ID);
        final Integer year = getIntegerParameter(request, PARAM_YEAR);
        final String acceptedName = getStringParameter(request, PARAM_ACCEPTED_NAME);
        logger.info("{} : params ({}='{}', {}='{}', {}='{}')", uuid, PARAM_PLAN_ID, planId, PARAM_YEAR, year, PARAM_ACCEPTED_NAME, acceptedName);
        if (planId == null) {
            logger.warn("{} : End. Not '{}' set", uuid, PARAM_PLAN_ID);
            response.getWriter().print("Не указан идентифкатор плана проверки");
            response.setStatus(500);
            return;
        }
        messageProcessor.processPlanRegular294Correction(uuid, response, planId, year, acceptedName);
    }


    private void processPlanRegular294Initialization(final HttpServletRequest request, final HttpServletResponse response, final String uuid)
            throws IOException {
        final Integer planId = getIntegerParameter(request, PARAM_PLAN_ID);
        final Integer year = getIntegerParameter(request, PARAM_YEAR);
        final String acceptedName = getStringParameter(request, PARAM_ACCEPTED_NAME);
        logger.info("{} : params ({}='{}', {}='{}', {}='{}')", uuid, PARAM_PLAN_ID, planId, PARAM_YEAR, year, PARAM_ACCEPTED_NAME, acceptedName);
        if (planId == null) {
            logger.warn("{} : End. Not '{}' set", uuid, PARAM_PLAN_ID);
            response.getWriter().print("Не указан идентифкатор плана проверки");
            response.setStatus(500);
            return;
        }
        messageProcessor.processPlanRegular294Initialization(uuid, response, planId, year, acceptedName);
    }

    private void processProsecutorAsk(final HttpServletRequest request, final HttpServletResponse response, final String uuid) throws IOException {
        messageProcessor.processProsecutorAsk(response, uuid);
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
