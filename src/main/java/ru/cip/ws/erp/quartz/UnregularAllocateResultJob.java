package ru.cip.ws.erp.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import ru.cip.ws.erp.business.MessageProcessor;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Author: Upatov Egor <br>
 * Date: 11.01.2017, 21:57 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Component
public class UnregularAllocateResultJob extends QuartzJobBean{
    private static final Logger log = LoggerFactory.getLogger("JOB");

    private static final AtomicLong counter = new AtomicLong(0);

    @Autowired
    private MessageProcessor messageProcessor;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final long logTag = counter.incrementAndGet();
        log.info("#{} Start Job[{}] by Trigger[{}]: AllocateResult unregular checks",
                logTag,
                jobExecutionContext.getJobDetail().getKey(),
                jobExecutionContext.getTrigger().getKey());
        final Map<String, String> result = messageProcessor.unregularResultAllocate(logTag, "Автоматическое размещенние результатов внеплановых проверок");
        log.info("#{} Finished Job[{}]: Result = {}", logTag, jobExecutionContext.getJobDetail().getKey(), result);
        jobExecutionContext.setResult(result);
    }
}
