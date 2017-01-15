package ru.cip.ws.erp.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import ru.cip.ws.erp.business.AllocationService;

/**
 * Author: Upatov Egor <br>
 * Date: 11.01.2017, 21:57 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Component
public class UnregularAllocationResultJob extends QuartzJobBean{
    private static final Logger log = LoggerFactory.getLogger("JOB");

    @Autowired
    private AllocationService allocationService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final TriggerKey triggerKey = jobExecutionContext.getTrigger().getKey();
        log.debug("Start Quartz Job  by Trigger[{}]: {}", triggerKey, allocationService.hashCode());
    }
}
