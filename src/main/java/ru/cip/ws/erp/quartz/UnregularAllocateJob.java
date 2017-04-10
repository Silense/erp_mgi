package ru.cip.ws.erp.quartz;

import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import ru.cip.ws.erp.ConfigurationHolder;
import ru.cip.ws.erp.business.MessageProcessor;
import ru.cip.ws.erp.jpa.dao.SystemSettingsDaoImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static ru.cip.ws.erp.ConfigurationHolder.CFG_KEY_ALLOCATE_BY_DATE;
import static ru.cip.ws.erp.ConfigurationHolder.CFG_KEY_ALLOCATE_BY_ORDER_NUM;
import static ru.cip.ws.erp.ConfigurationHolder.CFG_KEY_SCHEDULE_UNREGULAR_ALLOCATE_LAST_FIRE_DATE;

/**
 * Author: Upatov Egor <br>
 * Date: 11.01.2017, 21:57 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Component
@DisallowConcurrentExecution
public class UnregularAllocateJob extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger("JOB");
    private static final AtomicLong counter = new AtomicLong(0);
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private MessageProcessor messageProcessor;

    @Autowired
    private ConfigurationHolder cfg;

    @Autowired
    private SystemSettingsDaoImpl settingsDao;

    @Override
    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
        final long logTag = counter.incrementAndGet();
        log.info("#{} Start Job[{}] by Trigger[{}]", logTag, ctx.getJobDetail().getKey(), ctx.getTrigger().getKey());
        final String checkOrderNum = settingsDao.getString(cfg.getAppId(), CFG_KEY_ALLOCATE_BY_ORDER_NUM);
        final Date checkOrderDate = settingsDao.getDate(cfg.getAppId(), CFG_KEY_ALLOCATE_BY_DATE);
        log.info("#{} parameters from database ORDER_NUM='{}' ORDER_DATE='{}'", logTag, checkOrderNum, checkOrderDate);
        if (StringUtils.isNotEmpty(checkOrderNum) && checkOrderDate != null) {
            final Map<String, String> result = messageProcessor.unregularAllocate(
                    logTag,
                    "Ручное размещение внеплановой проверки с ORDER_NUM='" + checkOrderNum + "' и ORDER_DATE=" + sdf.format(checkOrderDate),
                    checkOrderNum,
                    checkOrderDate
            );
            log.info("#{} Finished Job[{}]: Result = {}", logTag, ctx.getJobDetail().getKey(), result);
            ctx.setResult(result);
        } else {
            final Date begDate = settingsDao.getDate(cfg.getAppId(), CFG_KEY_SCHEDULE_UNREGULAR_ALLOCATE_LAST_FIRE_DATE);
            final String dateInterval = sdf.format(begDate);
            final Map<String, String> result = messageProcessor.unregularAllocate(
                    logTag,
                    "Автоматическое размещение внеплановых проверок от " + dateInterval,
                    begDate
            );
            log.info("#{} Finished Job[{}]: Result = {}", logTag, ctx.getJobDetail().getKey(), result);
            ctx.setResult(result);
        }

    }
}
