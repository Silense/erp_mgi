package ru.cip.ws.erp.quartz;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static ru.cip.ws.erp.ConfigurationHolder.CFG_KEY_UNREGULAR_SCHEDULE_LAST_FIRE_DATE;

/**
 * Author: Upatov Egor <br>
 * Date: 11.01.2017, 21:57 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Component
@DisallowConcurrentExecution
public class UnregularAllocationJob extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger("JOB");
    private static final AtomicLong counter = new AtomicLong(0);

    @Autowired
    private MessageProcessor messageProcessor;

    @Autowired
    private ConfigurationHolder cfg;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final long logTag = counter.incrementAndGet();
        final Date previousFireDate = cfg.getDate(CFG_KEY_UNREGULAR_SCHEDULE_LAST_FIRE_DATE);
        final Date scheduledFireTime = jobExecutionContext.getScheduledFireTime();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.debug("#{} Start Job[{}] by Trigger[{}]: Allocate Unregular Checks from [{}] to [{}]",
                logTag,
                jobExecutionContext.getJobDetail().getKey(),
                jobExecutionContext.getTrigger().getKey(),
                sdf.format(previousFireDate),
                sdf.format(scheduledFireTime)
        );
        cfg.set(CFG_KEY_UNREGULAR_SCHEDULE_LAST_FIRE_DATE, scheduledFireTime);
        final Map<String, String> result = messageProcessor.unregularAllocate(logTag, previousFireDate, scheduledFireTime);
        log.info("#{} Finished Job[{}]: Result = {}", logTag, jobExecutionContext.getJobDetail().getKey(), result);
    }
}
