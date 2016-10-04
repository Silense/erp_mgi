package ru.cip.ws.erp.business;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.jdbc.dao.CheckPlanDaoImpl;
import ru.cip.ws.erp.jdbc.dao.CheckPlanRecordDaoImpl;
import ru.cip.ws.erp.jdbc.entity.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
    private CheckPlanDaoImpl checkPlanDao;
    @Autowired
    private CheckPlanRecordDaoImpl checkPlanRecordDao;

    public void processPlanRegular294Correction(
            final String requestId,
            final HttpServletResponse response,
            final Integer checkPlanId,
            final Integer year,
            final String acceptedName
    ) throws IOException {
        final CipCheckPlan checkPlan = checkPlanDao.getByIdFromView(checkPlanId);
        if (checkPlan == null) {
            logger.warn("#{} End. CheckPlan not found", requestId);
            response.getWriter().print("Не найден план проверки (по идентификатору)");
            response.setStatus(500);
            return;
        }
        logger.info("#{} founded CheckPlan: {}", requestId, checkPlan);
        final PlanCheckErp planCheckErp = checkPlanDao.getLastActiveByPlan(checkPlan);
        logger.info("#{} founded PlanCheckErp: {}", requestId, planCheckErp);
        if (planCheckErp == null || !StatusErp.isAnswered(planCheckErp.getStatus())) {
            logger.warn("#{} End. PLAN[{}] is not send for ERP", requestId, checkPlan.getId());
            response.getWriter().print(
                    String.format("Нельзя корректировать план: План %d еще не был первично выгружен в ЕРП", checkPlan.getId())
            );
            response.setStatus(500);
            return;
        }
        if (planCheckErp.getErpId() == null) {
            logger.warn("#{} End. PLAN[{}] is not send for ERP", requestId, checkPlan.getId());
            response.getWriter().print(
                    String.format(
                            "Нельзя корректировать план: По первичному размещению плана %d еще не было ответа из ЕРП", checkPlan.getId()
                    )
            );
            response.setStatus(500);
            return;
        }
        final List<CipCheckPlanRecord> checkPlanRecords = checkPlanRecordDao.getRecordsFromViewByPlanId(checkPlan.getId());
        if (checkPlanRecords.isEmpty()) {
            logger.warn("#{} End. Not found any PlanRecords by check_plan_id = {}", requestId, checkPlan.getId());
            response.getWriter().println(String.format("По плану проверок [%d] не найдено проверок", checkPlan.getId()));
            response.setStatus(500);
            response.flushBuffer();
            return;
        } else if (logger.isDebugEnabled()) {
            for (CipCheckPlanRecord checkPlanRecord : checkPlanRecords) {
                logger.info("#{} Founded record: {}", requestId, checkPlanRecord);
            }
        }
        final List<PlanCheckRecErp> sentCheckPlanRecords = checkPlanRecordDao.getRecordsByPlan(planCheckErp);
        final String result = messageService.sendPlanRegular294Correction(
                requestId, checkPlan, checkPlanRecords, planCheckErp, sentCheckPlanRecords, acceptedName, year
        );
        if (StringUtils.isNotEmpty(result)) {
            response.setContentType("text/xml");
            response.getWriter().println(result);
            response.setStatus(200);
        } else {
            response.getWriter().println("Ошибка");
            response.setStatus(500);
        }
    }

    public void processPlanRegular294Initialization(
            final HttpServletResponse response, final String requestId, final Integer checkPlanId, final Integer year, final String acceptedName
    ) throws IOException {
        final CipCheckPlan checkPlan = checkPlanDao.getByIdFromView(checkPlanId);
        if (checkPlan == null) {
            logger.warn("{} : End. CheckPlan[{}] not found ", requestId, checkPlanId);
            response.getWriter().print(String.format("Не найден план проверки [%s]", checkPlanId));
            response.setStatus(500);
            return;
        }
        logger.info("{} : founded CheckPlan: {}", requestId, checkPlan);
        final List<CipCheckPlanRecord> checkPlanRecords = checkPlanRecordDao.getRecordsFromViewByPlanId(checkPlan.getId());
        if (checkPlanRecords.isEmpty()) {
            logger.warn("{} : End. Not found any PlanRecords by check_plan_id = {}", requestId, checkPlan.getId());
            response.getWriter().println(String.format("По плану проверок [%d] не найдено проверок", checkPlan.getId()));
            response.setStatus(500);
            return;
        } else if (logger.isDebugEnabled()) {
            for (CipCheckPlanRecord checkPlanRecord : checkPlanRecords) {
                logger.info("{} : Founded record: {}", requestId, checkPlanRecord);
            }
        }
        final PlanCheckErp lastActiveByPlan = checkPlanDao.getLastActiveByPlan(checkPlan);
        if(lastActiveByPlan != null){
            response.setStatus(500);
            String message = "%d";
            switch (lastActiveByPlan.getStatus()){
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
            response.getWriter().println(sub_result);
            return;
        }
        final String result = messageService.sendPlanRegular294Initialization(requestId, checkPlan, acceptedName, year, checkPlanRecords);
        if (StringUtils.isNotEmpty(result)) {
            response.setContentType("text/xml");
            response.getWriter().println(result);
            response.setStatus(200);
        } else {
            response.getWriter().println("Ошибка");
            response.setStatus(500);
        }
    }

    public void processProsecutorAsk(final HttpServletResponse response, final String requestId) throws IOException {
        final String result = messageService.sendProsecutorAck(requestId);
        if (StringUtils.isNotEmpty(result)) {
            response.setContentType("text/xml");
            response.getWriter().println(result);
            response.setStatus(200);
        } else {
            response.getWriter().println("Ошибка");
            response.setStatus(500);
        }
    }
}
