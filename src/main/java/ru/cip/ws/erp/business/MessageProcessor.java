package ru.cip.ws.erp.business;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.factory.MessageFactory;
import ru.cip.ws.erp.factory.PropertiesHolder;
import ru.cip.ws.erp.jpa.dao.*;
import ru.cip.ws.erp.jpa.entity.*;
import ru.cip.ws.erp.jpa.entity.views.PlanAct;
import ru.cip.ws.erp.jpa.entity.views.PlanActViolation;
import ru.cip.ws.erp.jpa.entity.views.Plan;
import ru.cip.ws.erp.jpa.entity.views.PlanRecord;

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
    private PropertiesHolder prop;

    @Autowired
    private PlanDaoImpl planViewDao;

    @Autowired
    private PlanErpDaoImpl planDao;

    @Autowired
    private PlanRecordDaoImpl planRecordViewDao;

    @Autowired
    private PlanRecordErpDaoImpl planRecordDao;

    @Autowired
    private PlanActDaoImpl actDao;


    public void processProsecutorAsk(final HttpServletResponse response, final String requestId) throws IOException {
        final String result = messageService.sendProsecutorAck(
                requestId, "4.1.1 Запрос на получение справочника территориальных юрисдикций прокуратур Российской Федерации"
        );
        wrapResponse(response, result);
    }

    public void processPlanRegular294Initialization(
            final String requestId, final HttpServletResponse response, final Integer planId, final Integer year, final String acceptedName
    ) throws IOException {
        final Plan plan = planViewDao.getById(planId);
        if (plan == null) {
            logger.warn("{} : End. Plan not found", requestId);
            wrapErrorResponse(response, String.format("Не найден план проверки [%d]", planId));
            return;
        }
        logger.info("{} : founded Plan: {}", requestId, plan);

        final List<PlanRecord> planRecords = planRecordViewDao.getRecordsByPlan(plan);
        if (planRecords == null || planRecords.isEmpty()) {
            logger.warn("{} : End. Not found any PlanRecords by check_plan_id = {}", requestId, plan.getId());
            wrapErrorResponse(response, String.format("По плану проверок [%d] не найдено проверок", plan.getId()));
            return;
        } else if (logger.isDebugEnabled()) {
            for (PlanRecord planRecord : planRecords) {
                logger.info("{} : Founded record: {}", requestId, planRecord);
            }
        }

        final PlanErp lastActiveByPlan = planDao.getLastActiveByPlan(plan);
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
            final String sub_result = String.format(message, plan.getId());
            logger.warn("{} : End. Already was initialized or wait for response from ERP / {}", requestId, sub_result);
            wrapErrorResponse(response, sub_result);
            return;
        }
        final String result = messageService.sendPlanRegular294Initialization(
                requestId,
                "4.1.2 Запрос на первичное размещение плана плановых проверок",
                MessageFactory.constructMailer(prop.MGI_ORG_NAME, prop.MGI_OGRN, prop.MGI_FRGU_ORG_ID, prop.MGI_FRGU_SERV_ID),
                MessageFactory.constructAddressee(prop.ADDRESSEE_CODE, prop.ADDRESSEE_NAME),
                prop.KO_NAME,
                plan,
                acceptedName,
                year,
                planRecords
        );
        wrapResponse(response, result);
    }


    public void processPlanRegular294Correction(
            final String requestId, final HttpServletResponse response, final Integer planId, final Integer year, final String acceptedName
    ) throws IOException {
        final Plan plan = planViewDao.getById(planId);
        if (plan == null) {
            logger.warn("{} : End. Plan not found", requestId);
            wrapErrorResponse(response, String.format("Не найден план проверки [%d]", planId));
            return;
        }
        logger.info("{} : founded Plan: {}", requestId, plan);

        final PlanErp planErp = planDao.getLastActiveByPlanOrFault(plan);
        logger.info("{} : founded PlanErp: {}", requestId, planErp);

        if (planErp == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", requestId, plan.getId());
            wrapErrorResponse(
                    response, String.format("Нельзя корректировать план: План %d еще не был первично выгружен в ЕРП", plan.getId())
            );
            return;
        }
        if (planErp.getErpId() == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", requestId, plan.getId());
            wrapErrorResponse(
                    response, String.format(
                            "Нельзя корректировать план: По первичному размещению плана %d еще не было ответа из ЕРП", plan.getId()
                    )
            );
            return;
        }
        final List<PlanRecord> checkPlanRecords = planRecordViewDao.getRecordsByPlan(plan);
        if (checkPlanRecords.isEmpty()) {
            logger.warn("{} : End. Not found any PlanRecords by plan_id = {}", requestId, plan.getId());
            wrapErrorResponse(response, String.format("По плану проверок [%d] не найдено проверок", plan.getId()));
            return;
        } else if (logger.isDebugEnabled()) {
            for (PlanRecord checkPlanRecord : checkPlanRecords) {
                logger.info("{} :  Founded record: {}", requestId, checkPlanRecord);
            }
        }

        final List<PlanRecErp> sentCheckPlanRecords = planRecordDao.getRecordsByPlan(planErp);
        final Map<Integer, BigInteger> erpIDByCorrelatedID = new HashMap<>(sentCheckPlanRecords.size());
        for (PlanRecErp record : sentCheckPlanRecords) {
            erpIDByCorrelatedID.put(record.getCorrelationId(), record.getErpId());
        }

        final String result = messageService.sendPlanRegular294Correction(
                requestId,
                "4.1.3 Запрос на размещение корректировки плана плановых проверок",
                MessageFactory.constructMailer(prop.MGI_ORG_NAME, prop.MGI_OGRN, prop.MGI_FRGU_ORG_ID, prop.MGI_FRGU_SERV_ID),
                MessageFactory.constructAddressee(prop.ADDRESSEE_CODE, prop.ADDRESSEE_NAME),
                prop.KO_NAME,
                plan,
                planErp.getErpId(),
                StringUtils.defaultString(plan.getAcceptedName(), acceptedName),
                year != null ? year : Calendar.getInstance().get(Calendar.YEAR),
                checkPlanRecords,
                erpIDByCorrelatedID
        );
        wrapResponse(response, result);
    }


    public void processPlanResult294Initialization(
            final String requestId, final HttpServletResponse response, final Integer planId, final Integer year
    ) throws IOException {
        final Plan plan = planViewDao.getById(planId);
        if (plan == null) {
            logger.warn("{} : End. Plan not found", requestId);
            wrapErrorResponse(response, String.format("Не найден план проверки [%d]", planId));
            return;
        }
        logger.info("{} : founded Plan: {}", requestId, plan);

        final PlanErp planErp = planDao.getLastActiveByPlanOrFault(plan);
        logger.info("{} : founded PlanErp: {}", requestId, planErp);

        if (planErp == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", requestId, plan.getId());
            wrapErrorResponse(
                    response, String.format("Нельзя корректировать план: План %d еще не был первично выгружен в ЕРП", plan.getId())
            );
            return;
        }
        if (planErp.getErpId() == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", requestId, plan.getId());
            wrapErrorResponse(
                    response, String.format("Нельзя корректировать план: По первичному размещению плана %d еще не было ответа из ЕРП", plan.getId())
            );
            return;
        }
        final Map<PlanAct, List<PlanActViolation>> actMap = actDao.getWithViolationsByPlan(plan);
        final List<PlanRecErp> sentPlanRecords = planRecordDao.getRecordsByPlan(planErp);
        final Map<Integer, BigInteger> erpIDByCorrelatedID = new HashMap<>(sentPlanRecords.size());
        for (PlanRecErp record : sentPlanRecords) {
            erpIDByCorrelatedID.put(record.getCorrelationId(), record.getErpId());
        }
        final String result = messageService.sendPlanResult294Initialization(
                requestId,
                "4.1.2 Запрос на первичное размещение результатов по нескольким проверкам из плана",
                MessageFactory.constructMailer(prop.MGI_ORG_NAME, prop.MGI_OGRN, prop.MGI_FRGU_ORG_ID, prop.MGI_FRGU_SERV_ID),
                MessageFactory.constructAddressee(prop.ADDRESSEE_CODE, prop.ADDRESSEE_NAME),
                prop.KO_NAME,
                plan,
                planErp.getErpId(),
                year != null ? year : Calendar.getInstance().get(Calendar.YEAR),
                actMap,
                erpIDByCorrelatedID
        );
        wrapResponse(response, result);
    }

    public void processPlanResult294Correction(
            final String requestId, final HttpServletResponse response, final Integer planId, final Integer year
    ) throws IOException {
        final Plan plan = planViewDao.getById(planId);
        if (plan == null) {
            logger.warn("{} : End. Plan not found", requestId);
            wrapErrorResponse(response, String.format("Не найден план проверки [%d]", planId));
            return;
        }
        logger.info("{} : founded Plan: {}", requestId, plan);

        PlanErp planErp = planDao.getLastActiveByPlanOrFault(plan);
        logger.info("{} : founded PlanErp: {}", requestId, planErp);

        if (planErp == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", requestId, plan.getId());
            wrapErrorResponse(
                    response, String.format("Нельзя корректировать план: План %d еще не был первично выгружен в ЕРП", plan.getId())
            );
            return;
        }
        if (planErp.getErpId() == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", requestId, plan.getId());
            wrapErrorResponse(
                    response, String.format(
                            "Нельзя корректировать план: По первичному размещению плана %d еще не было ответа из ЕРП", plan.getId()
                    )
            );
            return;
        }

        final Map<PlanAct, List<PlanActViolation>> actMap = actDao.getWithViolationsByPlan(plan);
        final List<PlanRecErp> sentPlanRecords = planRecordDao.getRecordsByPlan(planErp);
        final Map<Integer, BigInteger> erpIDByCorrelatedID = new HashMap<>(sentPlanRecords.size());
        for (PlanRecErp record : sentPlanRecords) {
            erpIDByCorrelatedID.put(record.getCorrelationId(), record.getErpId());
        }
        final String result = messageService.sendPlanResult294Correction(
                requestId,
                "4.1.6 Запрос на размещение корректировки результатов проверкам плана ",
                MessageFactory.constructMailer(prop.MGI_ORG_NAME, prop.MGI_OGRN, prop.MGI_FRGU_ORG_ID, prop.MGI_FRGU_SERV_ID),
                MessageFactory.constructAddressee(prop.ADDRESSEE_CODE, prop.ADDRESSEE_NAME),
                plan,
                planErp.getErpId(),
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
