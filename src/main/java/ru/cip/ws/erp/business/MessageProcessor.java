package ru.cip.ws.erp.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.ConfigurationHolder;
import ru.cip.ws.erp.dto.AllocateUnregularParameter;
import ru.cip.ws.erp.dto.AllocateUnregularResultParameter;
import ru.cip.ws.erp.generated.erptypes.MessageToERPModelType;
import ru.cip.ws.erp.jpa.dao.PlanActDaoImpl;
import ru.cip.ws.erp.jpa.dao.UplanDaoImpl;
import ru.cip.ws.erp.jpa.entity.views.Uplan;
import ru.cip.ws.erp.jpa.entity.views.UplanAct;
import ru.cip.ws.erp.jpa.entity.views.UplanActViolation;

import java.text.SimpleDateFormat;
import java.util.*;

import static ru.cip.ws.erp.ConfigurationHolder.CFG_KEY_MGI_NAME;

/**
 * Author: Upatov Egor <br>
 * Date: 22.09.2016, 14:30 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
public class MessageProcessor {

    private final static Logger log = LoggerFactory.getLogger(MessageProcessor.class);
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private AllocationService allocationService;

    @Autowired
    private ConfigurationHolder cfg;

    @Autowired
    private UplanDaoImpl uplanDao;

    @Autowired
    private PlanActDaoImpl actDao;

    public Map<String, String> unregularAllocate(
            final long logTag,
            final String description,
            final Date begDate,
            final Date endDate
    ) {
        log.info(
                "#{} Start unregularAllocate from [{}] to [{}]: {}",
                logTag,
                sdf.format(begDate),
                sdf.format(endDate),
                description
        );
        final List<Uplan> checks = uplanDao.getChecksByInterval(begDate, endDate);
        if (checks.isEmpty()) {
            log.info("#{} Finish unregularAllocate. No checks found for allocation", logTag);
            return Collections.emptyMap();
        }
        log.info("#{} Found {} checks for unregularAllocation", logTag, checks.size());
        final MessageToERPModelType.Mailer mailer = cfg.getMailer();
        final MessageToERPModelType.Addressee addressee = cfg.getAddressee();
        final Set<AllocateUnregularParameter> parameters = new HashSet<>(checks.size());
        for (Uplan check : checks) {
            log.info("#{}-{}:ORDER_NUM='{}'", logTag, check.getCHECK_ID(), check.getORDER_NUM());
            final AllocateUnregularParameter allocationParameter = new AllocateUnregularParameter();
            allocationParameter.setCheck(check);
            allocationParameter.setKO_NAME(cfg.get(CFG_KEY_MGI_NAME));
            allocationParameter.setRecords(check.getRecords());
            parameters.add(allocationParameter);
        }
        final Map<String, String> result = allocationService.allocateUnregularBatch(
                logTag,
                description,
                mailer,
                addressee,
                parameters
        );
        log.info("#{} Finish unregularAllocate. Result = {}", logTag, result);
        return result;
    }

    public Map<String, String> unregularAllocate(long logTag, final String description, final String orderNum, final Date orderDate ) {
        log.info("#{} Start unregularAllocate with ORDER_NUM: '{}' and ORDER_DATE='{}'. Desc = {}", logTag, orderNum, orderDate, description);
        final Uplan check = uplanDao.getByOrderNumAndOrderDate(orderNum, orderDate);
        if (check == null) {
            log.info("#{} Finish unregularAllocate. No check found for allocation", logTag);
            return Collections.emptyMap();
        }
        final MessageToERPModelType.Mailer mailer = cfg.getMailer();
        final MessageToERPModelType.Addressee addressee = cfg.getAddressee();
        final Set<AllocateUnregularParameter> parameters = new HashSet<>(1);
        log.info("#{}-{}:ORDER_NUM='{}'", logTag, check.getCHECK_ID(), check.getORDER_NUM());
        final AllocateUnregularParameter allocationParameter = new AllocateUnregularParameter();
        allocationParameter.setCheck(check);
        allocationParameter.setKO_NAME(cfg.get(CFG_KEY_MGI_NAME));
        allocationParameter.setRecords(check.getRecords());
        parameters.add(allocationParameter);
        final Map<String, String> result = allocationService.allocateUnregularBatch(
                logTag,
                description,
                mailer,
                addressee,
                parameters
        );
        log.info("#{} Finish unregularAllocate. Result = {}", logTag, result);
        return result;
    }

    public Map<String, String> unregularReAllocate(long logTag, String description) {
        log.info("#{} Start unregularReAllocate", logTag);
        final List<Uplan> checks = uplanDao.getPartialAllocated();
        if (checks.isEmpty()) {
            log.info("#{} Finish unregularAllocate. No checks found for ReAllocation", logTag);
            return Collections.emptyMap();
        }
        log.info("#{} Found {} checks for unregularReAllocation", logTag, checks.size());
        final MessageToERPModelType.Mailer mailer = cfg.getMailer();
        final MessageToERPModelType.Addressee addressee = cfg.getAddressee();
        final Set<AllocateUnregularParameter> parameters = new HashSet<>(checks.size());
        for (Uplan check : checks) {
            log.info("#{}-{}:ORDER_NUM='{}'", logTag, check.getCHECK_ID(), check.getORDER_NUM());
            final AllocateUnregularParameter allocationParameter = new AllocateUnregularParameter();
            allocationParameter.setCheck(check);
            allocationParameter.setKO_NAME(cfg.get(CFG_KEY_MGI_NAME));
            allocationParameter.setRecords(check.getRecords());
            parameters.add(allocationParameter);
        }
        final Map<String, String> result = allocationService.allocateUnregularBatch(
                logTag,
                description,
                mailer,
                addressee,
                parameters
        );
        log.info("#{} Finish unregularReAllocate. Result = {}", logTag, result);
        return result;
    }

    public Map<String, String> unregularResultAllocate(long logTag, String description) {
        log.info("#{} Start unregularAllocateResult", logTag);
        final List<Uplan> checks = uplanDao.getAllocated();
        if (checks.isEmpty()) {
            log.info("#{} Finish unregularAllocateResult. No checks found for ReAllocation", logTag);
            return Collections.emptyMap();
        }
        log.info("#{} Found {} checks for unregularAllocateResult", logTag, checks.size());
        final MessageToERPModelType.Mailer mailer = cfg.getMailer();
        final MessageToERPModelType.Addressee addressee = cfg.getAddressee();
        final Set<AllocateUnregularResultParameter> parameters = new HashSet<>(checks.size());
        for (Uplan check : checks) {
            log.info("#{}-{}: ORDER_NUM='{}'", logTag, check.getCHECK_ID(), check.getORDER_NUM());
            final UplanAct act = uplanDao.getAct(check);
            if(act == null){
                log.warn("#{}-{}: Skip cause no Act found", logTag, check.getCHECK_ID());
                continue;
            }
            log.info("#{}-{}: Act = {}", logTag, check.getCHECK_ID(), act.getACT_PLACE_CREATE());
            final Set<UplanActViolation> violations = uplanDao.getViolations(check, act);
            log.info("#{}-{}: Violation = {}",  logTag, check.getCHECK_ID(), violations.size());
            parameters.add(new AllocateUnregularResultParameter(check, act, violations, 0, check.getRecords()));
        }
        final Map<String, String> result = allocationService.allocateUnregularResultBatch(
                logTag,
                description,
                mailer,
                addressee,
                parameters
        );
        log.info("#{} Finish unregularAllocateResult. Result = {}", logTag, result);
        return result;
    }








    /*
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
    }  */
}
