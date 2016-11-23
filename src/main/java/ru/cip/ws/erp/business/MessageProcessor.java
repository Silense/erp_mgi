package ru.cip.ws.erp.business;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.factory.MessageFactory;
import ru.cip.ws.erp.factory.PropertiesHolder;
import ru.cip.ws.erp.jpa.dao.PlanActDaoImpl;
import ru.cip.ws.erp.jpa.dao.PlanDaoImpl;
import ru.cip.ws.erp.jpa.dao.PlanErpDaoImpl;
import ru.cip.ws.erp.jpa.entity.PlanErp;
import ru.cip.ws.erp.jpa.entity.PlanRecErp;
import ru.cip.ws.erp.jpa.entity.views.Plan;
import ru.cip.ws.erp.jpa.entity.views.PlanAct;
import ru.cip.ws.erp.jpa.entity.views.PlanActViolation;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    private PlanActDaoImpl actDao;

    private static void wrapResponse(final HttpServletResponse response, final String result) {
        if (StringUtils.isNotEmpty(result)) {
            try {
                response.setStatus(200);
                response.setContentType("text/xml");
                response.getWriter().println(result);
            } catch (IOException e) {
                logger.error("Error in wrapResponse", e);
            }
        } else {
            wrapErrorResponse(response, "Ошибка");
        }
    }

    private static void wrapErrorResponse(final HttpServletResponse response, final String message) {
        try {
            response.setStatus(500);
            response.getWriter().println(message);
        } catch (IOException e) {
            logger.error("Error in wrapResponse", e);
        }
    }

    public void processProsecutorAsk(final HttpServletResponse response, final String uuid) throws IOException {
        final String result = messageService.sendProsecutorAck(
                uuid, "4.1.1 Запрос на получение справочника территориальных юрисдикций прокуратур Российской Федерации"
        );
        wrapResponse(response, result);
    }

    public void processPlanRegular294Initialization(
            final String uuid, final HttpServletResponse response, final Integer planId, final Integer year, final String acceptedName
    ) {
        final Plan plan = planViewDao.getById(planId);
        logger.info("{} : founded Plan: {}", uuid, plan);
        if (!isPlanValid(uuid, response, planId, plan)) {
            return;
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
            logger.warn("{} : End. Already was initialized or wait for response from ERP / {}", uuid, sub_result);
            wrapErrorResponse(response, sub_result);
            return;
        }
        wrapResponse(
                response, messageService.sendPlanRegular294Initialization(
                        uuid,
                        "4.1.2 Запрос на первичное размещение плана плановых проверок",
                        MessageFactory.createMailer(prop.MGI_ORG_NAME, prop.MGI_OGRN, prop.MGI_FRGU_ORG_ID, prop.MGI_FRGU_SERV_ID),
                        MessageFactory.createAddressee(prop.ADDRESSEE_CODE, prop.ADDRESSEE_NAME),
                        prop.KO_NAME,
                        plan,
                        acceptedName,
                        year,
                        plan.getRecords()
                )
        );

    }

    private boolean isPlanValid(final String uuid, final HttpServletResponse response, final Integer planId, final Plan plan) {
        if (plan == null) {
            logger.warn("{} : End. Plan not found", uuid);
            wrapErrorResponse(response, String.format("Не найден план проверки [%d]", planId));
            return false;
        }
        if (plan.getRecords().isEmpty()) {
            logger.warn("{} : End. Not found any PlanRecords by plan_id = {}", uuid, plan.getId());
            wrapErrorResponse(response, String.format("По плану проверок [%d] не найдено проверок", plan.getId()));
            return false;
        }
        return true;
    }

    public void processPlanRegular294Correction(
            final String uuid, final HttpServletResponse response, final Integer planId, final Integer year, final String acceptedName
    ) {
        final Plan plan = planViewDao.getById(planId);
        logger.info("{} : founded Plan: {}", uuid, plan);
        if (!isPlanValid(uuid, response, planId, plan)) {
            return;
        }
        final PlanErp planErp = planDao.getLastActiveByPlanOrFault(plan);
        logger.info("{} : founded PlanErp: {}", uuid, planErp);
        if (planErp == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", uuid, plan.getId());
            wrapErrorResponse(
                    response, String.format("Нельзя корректировать план: План %d еще не был первично выгружен в ЕРП", plan.getId())
            );
            return;
        }
        if (planErp.getErpId() == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", uuid, plan.getId());
            wrapErrorResponse(
                    response, String.format(
                            "Нельзя корректировать план: По первичному размещению плана %d еще не было ответа из ЕРП", plan.getId()
                    )
            );
            return;
        }
        final Map<Long, BigInteger> erpIDMap = new HashMap<>(planErp.getRecords().size());
        for (PlanRecErp x : planErp.getRecords()) {
            erpIDMap.put(x.getCorrelationId(), x.getErpId());
        }
        wrapResponse(
                response, messageService.sendPlanRegular294Correction(
                        uuid,
                        "4.1.3 Запрос на размещение корректировки плана плановых проверок",
                        MessageFactory.createMailer(prop.MGI_ORG_NAME, prop.MGI_OGRN, prop.MGI_FRGU_ORG_ID, prop.MGI_FRGU_SERV_ID),
                        MessageFactory.createAddressee(prop.ADDRESSEE_CODE, prop.ADDRESSEE_NAME),
                        prop.KO_NAME,
                        plan,
                        planErp.getErpId(),
                        StringUtils.defaultString(plan.getAcceptedName(), acceptedName),
                        year != null ? year : Calendar.getInstance().get(Calendar.YEAR),
                        plan.getRecords(),
                        erpIDMap
                )
        );
    }

    public void processPlanResult294Initialization(final String uuid, final HttpServletResponse response, final Integer planId, final Integer year) {
        final Plan plan = planViewDao.getById(planId);
        logger.info("{} : founded Plan: {}", uuid, plan);
        if (!isPlanValid(uuid, response, planId, plan)) {
            return;
        }
        final PlanErp planErp = planDao.getLastActiveByPlanOrFault(plan);
        logger.info("{} : founded PlanErp: {}", uuid, planErp);
        if (planErp == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", uuid, plan.getId());
            wrapErrorResponse(
                    response, String.format("Нельзя корректировать план: План %d еще не был первично выгружен в ЕРП", plan.getId())
            );
            return;
        }
        if (planErp.getErpId() == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", uuid, plan.getId());
            wrapErrorResponse(
                    response, String.format("Нельзя корректировать план: По первичному размещению плана %d еще не было ответа из ЕРП", plan.getId())
            );
            return;
        }
        final Map<PlanAct, Set<PlanActViolation>> actMap = actDao.getWithViolationsByPlan(plan);
        final Map<Long, BigInteger> erpIDMap = new HashMap<>(planErp.getRecords().size());
        for (PlanRecErp record : planErp.getRecords()) {
            erpIDMap.put(record.getCorrelationId(), record.getErpId());
        }
        wrapResponse(
                response, messageService.sendPlanResult294Initialization(
                        uuid,
                        "4.1.2 Запрос на первичное размещение результатов по нескольким проверкам из плана",
                        MessageFactory.createMailer(prop.MGI_ORG_NAME, prop.MGI_OGRN, prop.MGI_FRGU_ORG_ID, prop.MGI_FRGU_SERV_ID),
                        MessageFactory.createAddressee(prop.ADDRESSEE_CODE, prop.ADDRESSEE_NAME),
                        prop.KO_NAME,
                        plan,
                        planErp.getErpId(),
                        year != null ? year : Calendar.getInstance().get(Calendar.YEAR),
                        actMap,
                        erpIDMap
                )
        );
    }

    public void processPlanResult294Correction(final String uuid, final HttpServletResponse response, final Integer planId, final Integer year) {
        final Plan plan = planViewDao.getById(planId);
        logger.info("{} : founded Plan: {}", uuid, plan);
        if (!isPlanValid(uuid, response, planId, plan)) {
            return;
        }

        final PlanErp planErp = planDao.getLastActiveByPlanOrFault(plan);
        logger.info("{} : founded PlanErp: {}", uuid, planErp);
        if (planErp == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", uuid, plan.getId());
            wrapErrorResponse(
                    response, String.format("Нельзя корректировать план: План %d еще не был первично выгружен в ЕРП", plan.getId())
            );
            return;
        }
        if (planErp.getErpId() == null) {
            logger.warn("{} : End. PLAN[{}] is not send for ERP", uuid, plan.getId());
            wrapErrorResponse(
                    response, String.format(
                            "Нельзя корректировать план: По первичному размещению плана %d еще не было ответа из ЕРП", plan.getId()
                    )
            );
            return;
        }

        final Map<PlanAct, Set<PlanActViolation>> actMap = actDao.getWithViolationsByPlan(plan);
        final Map<Long, BigInteger> erpIDMap = new HashMap<>(planErp.getRecords().size());
        for (PlanRecErp record : planErp.getRecords()) {
            erpIDMap.put(record.getCorrelationId(), record.getErpId());
        }
        wrapResponse(
                response, messageService.sendPlanResult294Correction(
                        uuid,
                        "4.1.6 Запрос на размещение корректировки результатов проверкам плана ",
                        MessageFactory.createMailer(prop.MGI_ORG_NAME, prop.MGI_OGRN, prop.MGI_FRGU_ORG_ID, prop.MGI_FRGU_SERV_ID),
                        MessageFactory.createAddressee(prop.ADDRESSEE_CODE, prop.ADDRESSEE_NAME),
                        plan,
                        planErp.getErpId(),
                        year != null ? year : Calendar.getInstance().get(Calendar.YEAR),
                        actMap,
                        erpIDMap
                )
        );
    }
}
