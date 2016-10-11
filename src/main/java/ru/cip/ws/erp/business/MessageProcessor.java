package ru.cip.ws.erp.business;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.jdbc.dao.*;
import ru.cip.ws.erp.jdbc.entity.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Upatov Egor <br>
 * Date: 22.09.2016, 14:30 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
public class MessageProcessor {

    private final static Logger logger = LoggerFactory.getLogger(MessageProcessor.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private CheckPlanDaoImpl planViewDao;

    @Autowired
    private PlanCheckErpDaoImpl planDao;

    @Autowired
    private CheckPlanRecordDaoImpl planRecordViewDao;

    @Autowired
    private PlanCheckRecordErpDaoImpl planRecordDao;

    @Autowired
    private ActCheckDaoImpl actDao;


    public void processProsecutorAsk(final HttpServletResponse response, final String requestId) throws IOException {
        final String result = messageService.sendProsecutorAck(
                requestId, "4.1.1 Запрос на получение справочника территориальных юрисдикций прокуратур Российской Федерации"
        );
        wrapResponse(response, result);
    }

    public void processPlanRegular294Initialization(
            final String requestId, final HttpServletResponse response, final Integer checkPlanId, final Integer year, final String acceptedName
    ) throws IOException {
        final CipCheckPlan checkPlan = planViewDao.getById(checkPlanId);
        if (checkPlan == null) {
            logger.warn("{} : End. CheckPlan not found", requestId);
            wrapErrorResponse(response, String.format("Не найден план проверки [%d]", checkPlanId));
            return;
        }
        logger.info("{} : founded CheckPlan: {}", requestId, checkPlan);

        final List<CipCheckPlanRecord> checkPlanRecords = planRecordViewDao.getRecordsByPlan(checkPlan);
        if (checkPlanRecords == null || checkPlanRecords.isEmpty()) {
            logger.warn("{} : End. Not found any PlanRecords by check_plan_id = {}", requestId, checkPlan.getId());
            wrapErrorResponse(response, String.format("По плану проверок [%d] не найдено проверок", checkPlan.getId()));
            return;
        } else if (logger.isDebugEnabled()) {
            for (CipCheckPlanRecord checkPlanRecord : checkPlanRecords) {
                logger.info("{} : Founded record: {}", requestId, checkPlanRecord);
            }
        }

        final PlanCheckErp lastActiveByPlan = planDao.getLastActiveByPlan(checkPlan);
        if (lastActiveByPlan != null) {
            String message = "%d";
            switch (lastActiveByPlan.getStatus()) {
                case WAIT:
                    message = "План проверок [%d] ожидает ответа";
                    break;
                case WAIT_FOR_CORRECTION:
                    message = "План проверок [%d] ожидает ответа по Коррекции";
                    break;
                case ACCEPTED:
                    message = "План проверок [%d] ожидает ответа (Подтверждение получено)";
                    break;
                case SENDED:
                    message = "План проверок [%d] уже отослан";
                    break;
                case SUCCEEDED:
                    message = "План проверок [%d] уже зарегистрирован";
                    break;
            }
            final String sub_result = String.format(message, checkPlan.getId());
            logger.warn("{} : End. Already was initialized or wait for response from ERP / {}", requestId, sub_result);
            wrapErrorResponse(response, sub_result);
            return;
        }
        final String result = messageService.sendPlanRegular294Initialization(
                requestId, "4.1.2 Запрос на первичное размещение плана плановых проверок", checkPlan, acceptedName, year, checkPlanRecords
        );
        wrapResponse(response, result);
    }


    public void processPlanRegular294Correction(
            final String requestId, final HttpServletResponse response, final Integer checkPlanId, final Integer year, final String acceptedName
    ) throws IOException {
        final CipCheckPlan checkPlan = planViewDao.getById(checkPlanId);
        if (checkPlan == null) {
            logger.warn("{} : End. CheckPlan not found", requestId);
            wrapErrorResponse(response, String.format("Не найден план проверки [%d]", checkPlanId));
            return;
        }
        logger.info("{} : founded CheckPlan: {}", requestId, checkPlan);

        PlanCheckErp planCheckErp = planDao.getLastActiveByPlanOrFault(checkPlan);
        logger.info("{} : founded PlanCheckErp: {}", requestId, planCheckErp);

        if (planCheckErp == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", requestId, checkPlan.getId());
            wrapErrorResponse(
                    response, String.format("Нельзя корректировать план: План %d еще не был первично выгружен в ЕРП", checkPlan.getId())
            );
            return;
        }
        if (planCheckErp.getErpId() == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", requestId, checkPlan.getId());
            wrapErrorResponse(
                    response, String.format(
                            "Нельзя корректировать план: По первичному размещению плана %d еще не было ответа из ЕРП", checkPlan.getId()
                    )
            );
            return;
        }
        final List<CipCheckPlanRecord> checkPlanRecords = planRecordViewDao.getRecordsByPlan(checkPlan);
        if (checkPlanRecords.isEmpty()) {
            logger.warn("{} : End. Not found any PlanRecords by check_plan_id = {}", requestId, checkPlan.getId());
            wrapErrorResponse(response, String.format("По плану проверок [%d] не найдено проверок", checkPlan.getId()));
            return;
        } else if (logger.isDebugEnabled()) {
            for (CipCheckPlanRecord checkPlanRecord : checkPlanRecords) {
                logger.info("{} :  Founded record: {}", requestId, checkPlanRecord);
            }
        }

        final List<PlanCheckRecErp> sentCheckPlanRecords = planRecordDao.getRecordsByPlan(planCheckErp);
        final Map<Integer, BigInteger> erpIDByCorrelatedID = new HashMap<>(sentCheckPlanRecords.size());
        for (PlanCheckRecErp record : sentCheckPlanRecords) {
            erpIDByCorrelatedID.put(record.getCorrelationId(), record.getErpId());
        }

        final String result = messageService.sendPlanRegular294Correction(
                requestId,
                "4.1.3 Запрос на размещение корректировки плана плановых проверок",
                checkPlan,
                planCheckErp.getErpId(),
                StringUtils.defaultString(checkPlan.getAcceptedName(), acceptedName),
                year != null ? year : Calendar.getInstance().get(Calendar.YEAR),
                checkPlanRecords,
                erpIDByCorrelatedID
        );
        wrapResponse(response, result);
    }


    public void processPlanResult294Initialization(
            final String requestId, final HttpServletResponse response, final Integer checkPlanId, final Integer year
    ) throws IOException {
        final CipCheckPlan checkPlan = planViewDao.getById(checkPlanId);
        if (checkPlan == null) {
            logger.warn("{} : End. CheckPlan not found", requestId);
            wrapErrorResponse(response, String.format("Не найден план проверки [%d]", checkPlanId));
            return;
        }
        logger.info("{} : founded CheckPlan: {}", requestId, checkPlan);

        PlanCheckErp planCheckErp = planDao.getLastActiveByPlanOrFault(checkPlan);
        logger.info("{} : founded PlanCheckErp: {}", requestId, planCheckErp);

        if (planCheckErp == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", requestId, checkPlan.getId());
            wrapErrorResponse(
                    response, String.format("Нельзя корректировать план: План %d еще не был первично выгружен в ЕРП", checkPlan.getId())
            );
            return;
        }
        if (planCheckErp.getErpId() == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", requestId, checkPlan.getId());
            wrapErrorResponse(
                    response, String.format(
                            "Нельзя корректировать план: По первичному размещению плана %d еще не было ответа из ЕРП", checkPlan.getId()
                    )
            );
            return;
        }

        final Map<CipActCheck, List<CipActCheckViolation>> actMap = actDao.getWithViolationsByPlan(checkPlan);
        final List<PlanCheckRecErp> sentCheckPlanRecords = planRecordDao.getRecordsByPlan(planCheckErp);
        final Map<Integer, BigInteger> erpIDByCorrelatedID = new HashMap<>(sentCheckPlanRecords.size());
        for (PlanCheckRecErp record : sentCheckPlanRecords) {
            erpIDByCorrelatedID.put(record.getCorrelationId(), record.getErpId());
        }
        final String result = messageService.sendPlanResult294Initialization(
                requestId,
                "4.1.2 Запрос на первичное размещение результатов по нескольким проверкам из плана",
                checkPlan,
                planCheckErp.getErpId(),
                year != null ? year : Calendar.getInstance().get(Calendar.YEAR),
                actMap,
                erpIDByCorrelatedID
        );
        wrapResponse(response, result);
    }



    private static void wrapResponse(final HttpServletResponse response, final String result) throws IOException {
        if (StringUtils.isNotEmpty(result)) {
            response.setContentType("text/xml");
            response.getWriter().println(result);
            response.setStatus(200);
        } else {
            wrapErrorResponse(response, "Ошибка");
        }
    }

    private static void wrapErrorResponse(final HttpServletResponse response, final String message) throws IOException {
        response.getWriter().println(message);
        response.setStatus(500);
    }

}
