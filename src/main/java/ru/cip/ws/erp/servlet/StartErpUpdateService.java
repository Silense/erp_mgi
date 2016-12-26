package ru.cip.ws.erp.servlet;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.cip.ws.erp.business.MessageProcessor;
import ru.cip.ws.erp.business.TestMessageProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static ru.cip.ws.erp.servlet.ParameterNames.*;

/**
 * Author: Upatov Egor <br>
 * Date: 10.09.2016, 16:10 <br>
 * Description:
 */

@RestController("/update/start")
public class StartErpUpdateService {

    private static final Logger log = LoggerFactory.getLogger(StartErpUpdateService.class);

    private static final AtomicLong counter = new AtomicLong(0);


    @Autowired
    private MessageProcessor messageProcessor;
    @Autowired
    private TestMessageProcessor testMessageProcessor;


    @RequestMapping(params = {"DATA_KIND=PROSECUTOR_ACK"}, produces = {MimeTypeUtils.TEXT_PLAIN_VALUE})
    @ResponseBody
    public String processProsecutorAsk() throws IOException {
        final long requestNumber = counter.incrementAndGet();
        log.info("#{} : START processing PROSECUTOR_ACK request", requestNumber);
        long startTime = System.currentTimeMillis();
        final String result = testMessageProcessor.processProsecutorAsk(requestNumber);
        log.info("#{} : END. Result = '{}' [in {} ms]", requestNumber, result, System.currentTimeMillis()-startTime);
        return result;
    }

    @RequestMapping(params = {"DATA_KIND=UPLAN_UNREGULAR_294_INITIALIZATION"})
    @ResponseBody
    public String processUplanUnRegular294Initialization(
            @RequestParam(value = PARAM_TEST, required = false) String paramTest,
            @RequestParam(value = "SEVERAL_CHECKS_FROM") Integer fromId,
            @RequestParam(value = "SEVERAL_CHECKS_TO") Integer toId
    ) throws IOException {
        final long requestNumber = counter.incrementAndGet();
        log.info("#{} : START processing UPLAN_UNREGULAR_294_INITIALIZATION request", requestNumber);
        long startTime = System.currentTimeMillis();
        final boolean isTestMode = BooleanUtils.toBoolean(BooleanUtils.toBooleanObject(paramTest));
        final Map<String, String> result;
        if (isTestMode) {
            result = testMessageProcessor.processUplanUnRegular294Initialization(requestNumber, fromId, toId);
        } else {
            //TODO
            result = new LinkedHashMap<>();
        }
        log.info("#{} : END. Result = '{}' [in {} ms]", requestNumber, result, System.currentTimeMillis()-startTime);
        return result.toString();
    }

    @RequestMapping(params = {"DATA_KIND=UPLAN_UNREGULAR_294_CORRECTION"})
    @ResponseBody
    public String processUplanUnRegular294Correction(
            @RequestParam(value = PARAM_TEST, required = false) String paramTest,
            @RequestParam(value = "SEVERAL_CHECKS_FROM") Integer fromId,
            @RequestParam(value = "SEVERAL_CHECKS_TO") Integer toId
    ) throws IOException {
        final long requestNumber = counter.incrementAndGet();
        log.info("#{} : START processing UPLAN_UNREGULAR_294_CORRECTION request", requestNumber);
        long startTime = System.currentTimeMillis();
        final boolean isTestMode = BooleanUtils.toBoolean(BooleanUtils.toBooleanObject(paramTest));
        final Map<String, String> result;
        if (isTestMode) {
            result = testMessageProcessor.processUplanUnRegular294Correction(requestNumber, fromId, toId);
        } else {
            //TODO
            result = new LinkedHashMap<>();
        }
        log.info("#{} : END. Result = '{}' [in {} ms]", requestNumber, result, System.currentTimeMillis()-startTime);
        return result.toString();
    }


    @RequestMapping(params = {"DATA_KIND=UPLAN_RESULT_294_INITIALIZATION"})
    @ResponseBody
    public String processUplanResult294Initialization(
            @RequestParam(value = PARAM_TEST, required = false) String paramTest,
            @RequestParam(value = "SEVERAL_CHECKS_FROM") Integer fromId,
            @RequestParam(value = "SEVERAL_CHECKS_TO") Integer toId
    ) throws IOException {
        final long requestNumber = counter.incrementAndGet();
        log.info("#{} : START processing UPLAN_RESULT_294_INITIALIZATION request", requestNumber);
        long startTime = System.currentTimeMillis();
        final boolean isTestMode = BooleanUtils.toBoolean(BooleanUtils.toBooleanObject(paramTest));
        final Map<String, String> result;
        if (isTestMode) {
            result = testMessageProcessor.processUplanResult294Initialization(requestNumber, fromId, toId);
        } else {
            //TODO
            result = new LinkedHashMap<>();
        }
        log.info("#{} : END. Result = '{}' [in {} ms]", requestNumber, result, System.currentTimeMillis()-startTime);
        return result.toString();
    }




    @RequestMapping(params = {"DATA_KIND=PLAN_REGULAR_294_INITIALIZATION"}, produces = {MimeTypeUtils.TEXT_PLAIN_VALUE})
    @ResponseBody
    public String processPlanRegular294Initialization(
            @RequestParam(value = PARAM_TEST, required = false) String paramTest,
            @RequestParam(value = PARAM_PLAN_ID, required = false) Integer paramPlanId

    ) throws IOException {
        final long requestNumber = counter.incrementAndGet();
        log.info("#{} : START processing PLAN_REGULAR_294_INITIALIZATION request", requestNumber);
        long startTime = System.currentTimeMillis();
        final boolean isTestMode = BooleanUtils.toBoolean(BooleanUtils.toBooleanObject(paramTest));
        final String result;
        if (isTestMode) {
            result = testMessageProcessor.processPlanRegular294Initialization();
        } else {
            result = "";//processPlanRegular294Initialization(request, response, uuid);
        }
        log.info("#{} : END. Result = '{}' [in {} ms]", requestNumber, result, System.currentTimeMillis()-startTime);
        return result;
    }



//    @RequestMapping
//    public void handleRequest(
//            @RequestParam(PARAM_DATA_KIND) DataKindEnum dataKind,
//            @RequestParam(value = PARAM_TEST, required = false) String paramIsTest,
//            final HttpServletRequest request,
//            final HttpServletResponse response
//    ) throws ServletException, IOException {
//        final String uuid = UUID.randomUUID().toString();
//        response.setContentType("text/html");
//        response.setCharacterEncoding("UTF-8");
//        final String param_data_kind = getStringParameter(request, PARAM_DATA_KIND);
//        log.info("{} : Start handleRequest({}=\'{}\')", uuid, PARAM_DATA_KIND, param_data_kind);
//        final DataKindEnum data_kind = DataKindEnum.getEnum(param_data_kind);
//        if (data_kind == null) {
//            log.error("{} : End. Unknown {} = \'{}\' parameter value. Skip processing", uuid, PARAM_DATA_KIND, param_data_kind);
//            response.getWriter().println(String.format("Параметер %s имеет неизвестное значение \'%s\'", PARAM_DATA_KIND, param_data_kind));
//            response.setStatus(500);
//            return;
//        }
//
//        if (isTestMode) {
//            log.warn("{} : IN TEST_MODE WE MUST SEND REFERENCE EXAMPLE MESSAGE", uuid);
//        }
//        switch (data_kind) {
//           case PLAN_REGULAR_294_INITIALIZATION:
//
//                break;
//            case PLAN_REGULAR_294_CORRECTION:
//                if (isTestMode) {
//                    testMessageProcessor.processPlanRegular294Correction(uuid, response);
//                } else {
//                    processPlanRegular294Correction(request, response, uuid);
//                }
//                break;
//            case PLAN_RESULT_294_INITIALIZATION:
//                if (isTestMode) {
//                    testMessageProcessor.processPlanResult294Initialization(uuid, response);
//                } else {
//                    processPlanResult294Initialization(request, response, uuid);
//                }
//                break;
//            case PLAN_RESULT_294_CORRECTION:
//                if (isTestMode) {
//                    testMessageProcessor.processPlanResult294Correction(uuid, response);
//                } else {
//                    processPlanResult294Correction(request, response, uuid);
//                }
//                break;
//            // Внеплановые проверки
//            case UPLAN_UNREGULAR_294_INITIALIZATION:
//                if (isTestMode) {
//                    testMessageProcessor.processUplanUnRegular294Initialization(uuid, response);
//                } else {
//                    processUplanUnRegular294Initialization(request, response, uuid);
//                }
//                break;
//            case UPLAN_UNREGULAR_294_CORRECTION:
//                if (isTestMode) {
//                    testMessageProcessor.processUplanUnRegular294Correction(uuid, response);
//                } else {
//                    processUplanUnRegular294Correction(request, response, uuid);
//                }
//                break;
//            case UPLAN_RESULT_294_INITIALIZATION:
//                if (isTestMode) {
//                    testMessageProcessor.processUplanResult294Initialization(uuid, response);
//                } else {
//                    processUplanResult294Initialization(request, response, uuid);
//                }
//                break;
//            case UPLAN_RESULT_294_CORRECTION:
//                if (isTestMode) {
//                    testMessageProcessor.processUplanResult294Correction(uuid, response);
//                } else {
//                    processUplanResult294Correction(request, response, uuid);
//                }
//                break;
//            default:
//                log.error("{} : End. Unknown {} = \'{}\' parameter value. Skip processing", uuid, PARAM_DATA_KIND, param_data_kind);
//                response.getWriter().println(String.format("Параметер %s имеет неизвестное значение \'%s\'", PARAM_DATA_KIND, param_data_kind));
//                response.setStatus(400);
//                break;
//        }
//        log.info("{} : End.", uuid);
//    }

    private void processPlanResult294Correction(final HttpServletRequest request, final HttpServletResponse response, final String uuid)
            throws IOException {
        final Integer planId = getIntegerParameter(request, PARAM_PLAN_ID);
        final Integer year = getIntegerParameter(request, PARAM_YEAR);
        log.info("{} : params ({}='{}', {}='{}', {}='{}')", uuid, PARAM_PLAN_ID, planId, PARAM_YEAR, year);
        if (planId == null) {
            log.warn("{} : End. Not '{}' set", uuid, PARAM_PLAN_ID);
            response.getWriter().print("Не указан идентифкатор плана проверки");
            response.setStatus(500);
            return;
        }
       // messageProcessor.processPlanResult294Correction(uuid, response, planId, year);
    }

    private void processPlanResult294Initialization(final HttpServletRequest request, final HttpServletResponse response, final String uuid)
            throws IOException {
        final Integer planId = getIntegerParameter(request, PARAM_PLAN_ID);
        final Integer year = getIntegerParameter(request, PARAM_YEAR);
        log.info("{} : params ({}='{}', {}='{}', {}='{}')", uuid, PARAM_PLAN_ID, planId, PARAM_YEAR, year);
        if (planId == null) {
            log.warn("{} : End. Not '{}' set", uuid, PARAM_PLAN_ID);
            response.getWriter().print("Не указан идентифкатор плана проверки");
            response.setStatus(500);
            return;
        }
        //messageProcessor.processPlanResult294Initialization(uuid, response, planId, year);
    }

    private void processPlanRegular294Correction(final HttpServletRequest request, final HttpServletResponse response, final String uuid)
            throws IOException {
        final Integer planId = getIntegerParameter(request, PARAM_PLAN_ID);
        final Integer year = getIntegerParameter(request, PARAM_YEAR);
        final String acceptedName = getStringParameter(request, PARAM_ACCEPTED_NAME);
        log.info("{} : params ({}='{}', {}='{}', {}='{}')", uuid, PARAM_PLAN_ID, planId, PARAM_YEAR, year, PARAM_ACCEPTED_NAME, acceptedName);
        if (planId == null) {
            log.warn("{} : End. Not '{}' set", uuid, PARAM_PLAN_ID);
            response.getWriter().print("Не указан идентифкатор плана проверки");
            response.setStatus(500);
            return;
        }
       // messageProcessor.processPlanRegular294Correction(uuid, response, planId, year, acceptedName);
    }


    private void processPlanRegular294Initialization(final HttpServletRequest request, final HttpServletResponse response, final String uuid)
            throws IOException {
        final Integer planId = getIntegerParameter(request, PARAM_PLAN_ID);
        final Integer year = getIntegerParameter(request, PARAM_YEAR);
        final String acceptedName = getStringParameter(request, PARAM_ACCEPTED_NAME);
        log.info("{} : params ({}='{}', {}='{}', {}='{}')", uuid, PARAM_PLAN_ID, planId, PARAM_YEAR, year, PARAM_ACCEPTED_NAME, acceptedName);
        if (planId == null) {
            log.warn("{} : End. Not '{}' set", uuid, PARAM_PLAN_ID);
            response.getWriter().print("Не указан идентифкатор плана проверки");
            response.setStatus(500);
            return;
        }
        //messageProcessor.processPlanRegular294Initialization(uuid, response, planId, year, acceptedName);
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
                log.warn("Cannot cast parameter \'{}\' with value \'{}\' to integer", parameterName, parameterValueStr);
            }
        }
        return null;
    }

}
