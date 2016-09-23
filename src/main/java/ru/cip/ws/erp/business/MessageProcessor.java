package ru.cip.ws.erp.business;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.jdbc.dao.CheckPlanDaoImpl;
import ru.cip.ws.erp.jdbc.dao.CheckPlanRecordDaoImpl;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlan;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlanRecord;
import ru.cip.ws.erp.jdbc.entity.PlanCheckErp;
import ru.cip.ws.erp.jdbc.entity.PlanCheckRecErp;

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
            final String requestId, final HttpServletResponse response, final Integer checkPlanId, final Integer year, final String acceptedName
    ) throws IOException {
        final CipCheckPlan checkPlan = checkPlanDao.getByIdFromView(checkPlanId);
        if (checkPlan == null) {
            logger.warn("#{} End. CheckPlan not found", requestId);
            response.getWriter().print("Не найден план проверки (по идентификатору)");
            response.setStatus(404);
            return;
        }
        logger.debug("#{} founded CheckPlan: {}", requestId, checkPlan);
        final PlanCheckErp planCheckErp = checkPlanDao.getById(checkPlan.getCHECK_PLAN_ID());
        if (planCheckErp == null) {
            logger.warn("#{} End. PLAN[{}] is not send for ERP", requestId, checkPlan.getCHECK_PLAN_ID());
            response.getWriter().print(
                    String.format("Нельзя корректировать план: План %d еще не был первично выгружен в ЕРП", checkPlan.getCHECK_PLAN_ID())
            );
            response.setStatus(400);
            return;
        }
        logger.debug("#{} founded PlanCheckErp: {}", requestId, planCheckErp);
        if (planCheckErp.getCodeCheckPlanErp() == null) {
            logger.warn("#{} End. PLAN[{}] is not send for ERP", requestId, checkPlan.getCHECK_PLAN_ID());
            response.getWriter().print(
                    String.format(
                            "Нельзя корректировать план: По первичному размещению плана %d еще не было ответа из ЕРП", checkPlan.getCHECK_PLAN_ID()
                    )
            );
            response.setStatus(400);
            return;
        }
        final List<CipCheckPlanRecord> checkPlanRecords = checkPlanRecordDao.getRecordsFromViewByPlanId(checkPlan.getCHECK_PLAN_ID());
        if (checkPlanRecords.isEmpty()) {
            logger.warn("#{} End. Not found any PlanRecords by check_plan_id = {}", requestId, checkPlan.getCHECK_PLAN_ID());
            response.getWriter().println(String.format("По плану проверок [%d] не найдено проверок", checkPlan.getCHECK_PLAN_ID()));
            response.setStatus(404);
            response.flushBuffer();
            return;
        } else if (logger.isDebugEnabled()) {
            for (CipCheckPlanRecord checkPlanRecord : checkPlanRecords) {
                logger.debug("#{} Founded record: {}", requestId, checkPlanRecord);
            }
        }
        final List<PlanCheckRecErp> sentCheckPlanRecords = checkPlanRecordDao.getRecordsByPlanId(planCheckErp.getIdCheckPlanErp());
        final String result = messageService.sendPlanRegular294Correction(
                requestId,
                checkPlan,
                checkPlanRecords,
                planCheckErp,
                sentCheckPlanRecords,
                acceptedName,
                year
        );
        if (StringUtils.isNotEmpty(result)) {
            response.setContentType("text/xml");
            response.getWriter().println(result);
            response.setStatus(200);
        } else {
            response.setContentType("text/xml");
            response.getWriter().println("Ошибка");
            response.setStatus(500);
        }
    }

    public void processPlanRegular294Initialization(
            final HttpServletResponse response, final String requestId, final Integer checkPlanId, final Integer year, final String acceptedName
    ) throws IOException {
        final CipCheckPlan checkPlan = (year != null) ? checkPlanDao.getByYearFromView(year) : checkPlanDao.getByIdFromView(checkPlanId);
        if (checkPlan == null) {
            logger.warn("{} : End. CheckPlan not found", requestId);
            response.getWriter().print("Не найден план проверки " + (year != null ? "(по году)" : "(по идентификатору)"));
            response.setStatus(404);
            return;
        }
        logger.debug("{} : founded CheckPlan: {}", requestId, checkPlan);
        final List<CipCheckPlanRecord> checkPlanRecords = checkPlanRecordDao.getRecordsFromViewByPlanId(checkPlan.getCHECK_PLAN_ID());
        if (checkPlanRecords.isEmpty()) {
            logger.warn("{} : End. Not found any PlanRecords by check_plan_id = {}", requestId, checkPlan.getCHECK_PLAN_ID());
            response.getWriter().println(String.format("По плану проверок [%d] не найдено проверок", checkPlan.getCHECK_PLAN_ID()));
            response.setStatus(404);
            return;
        } else if (logger.isDebugEnabled()) {
            for (CipCheckPlanRecord checkPlanRecord : checkPlanRecords) {
                logger.debug("{} : Founded record: {}", requestId, checkPlanRecord);
            }
        }
        final String result = messageService.sendPlanRegular294Initialization(requestId, checkPlan, acceptedName, year, checkPlanRecords);
        if (StringUtils.isNotEmpty(result)) {
            response.setContentType("text/xml");
            response.getWriter().println(result);
            response.setStatus(200);
        } else {
            response.setContentType("text/xml");
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
            response.setContentType("text/xml");
            response.getWriter().println("Ошибка");
            response.setStatus(500);
        }
    }
}
