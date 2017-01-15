package ru.cip.ws.erp.config;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import ru.cip.ws.erp.ConfigurationHolder;
import ru.cip.ws.erp.quartz.AutowiringSpringBeanJobFactory;
import ru.cip.ws.erp.quartz.UnregularAllocationJob;
import ru.cip.ws.erp.quartz.UnregularAllocationResultJob;

import java.text.ParseException;
import java.util.Properties;

/**
 * Author: Upatov Egor <br>
 * Date: 11.01.2017, 23:46 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Configuration
public class SchedulerConfig {
    private static final Logger log = LoggerFactory.getLogger("CONFIG");



    @Bean(name="unregularAllocationJobDetail")
    public JobDetail unregularAllocationJob(){
        final JobDetailImpl result = new JobDetailImpl();
        result.setJobClass(UnregularAllocationJob.class);
        result.setKey(JobKey.jobKey("allocationJob", "unregular"));
        log.info("Create JobDetail[{}]", result.getKey());
        return result;
    }

    @Bean(name="unregularAllocationResultJobDetail")
    public JobDetail unregularAllocationResultJob(){
        final JobDetailImpl result = new JobDetailImpl();
        result.setJobClass(UnregularAllocationResultJob.class);
        result.setKey(JobKey.jobKey("allocationResultJob", "unregular"));
        log.info("Create JobDetail[{}]", result.getKey());
        return result;
    }


    @Bean(name="unregularAllocationResultTrigger")
    public CronTriggerFactoryBean unregularAllocationResultTrigger(
            final ConfigurationHolder cfg,
            @Qualifier("unregularAllocationResultJobDetail") final JobDetail jobDetail
    ) throws ParseException {
        final TriggerKey triggerKey = TriggerKey.triggerKey("allocationResultTrigger", "unregular");
        final CronTriggerFactoryBean result = new CronTriggerFactoryBean();
        result.setJobDetail(jobDetail);
        result.setName(triggerKey.getName());
        result.setGroup(triggerKey.getGroup());
        result.setCronExpression(cfg.get(ConfigurationHolder.CFG_KEY_UNREGULAR_RESULT_SCHEDULE));
        log.info("Create Trigger[{}] with cron='{}' for Job[{}]",
                triggerKey,
                cfg.get(ConfigurationHolder.CFG_KEY_UNREGULAR_RESULT_SCHEDULE),
                jobDetail.getKey()
        );
        return result;
    }



    @Bean(name="unregularAllocationTrigger")
    public CronTriggerFactoryBean unregularAllocationTrigger(
            final ConfigurationHolder cfg,
            @Qualifier("unregularAllocationJobDetail") final JobDetail jobDetail
    ) throws ParseException {
        final TriggerKey triggerKey = TriggerKey.triggerKey("allocationTrigger", "unregular");
        final CronTriggerFactoryBean result = new CronTriggerFactoryBean();
        result.setJobDetail(jobDetail);
        result.setName(triggerKey.getName());
        result.setGroup(triggerKey.getGroup());
        result.setCronExpression(cfg.get(ConfigurationHolder.CFG_KEY_UNREGULAR_SCHEDULE));
        log.info("Create Trigger[{}] with cron='{}' for Job[{}]",
                triggerKey,
                cfg.get(ConfigurationHolder.CFG_KEY_UNREGULAR_SCHEDULE),
                jobDetail.getKey()
        );
        return result;
    }

    @Bean(name="autowiringJobFactory")
    public JobFactory autowiringSpringBeanJobFactory(final ApplicationContext context){
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(context);
        return jobFactory;
    }


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(
            @Qualifier("unregularAllocationTrigger") final Trigger unregularAllocationTrigger,
            @Qualifier("unregularAllocationResultTrigger") final Trigger unregularResultAllocationTrigger,
            final JobFactory jobFactory
    ) {
        final SchedulerFactoryBean result = new SchedulerFactoryBean();
        result.setJobFactory(jobFactory);
        final Properties props  =  new Properties();
        props.setProperty(StdSchedulerFactory.PROP_SCHED_SKIP_UPDATE_CHECK,"true");
        result.setQuartzProperties(props);
        result.setTriggers(unregularAllocationTrigger, unregularResultAllocationTrigger);
        log.info("Create ScheduleFactory[@{}]", Integer.toHexString(result.hashCode()));
        return result;
    }


}
