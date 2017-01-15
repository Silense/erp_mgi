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
import ru.cip.ws.erp.quartz.UnregularAllocateJob;
import ru.cip.ws.erp.quartz.UnregularAllocateResultJob;
import ru.cip.ws.erp.quartz.UnregularReAllocateJob;

import java.text.ParseException;
import java.util.Properties;

import static ru.cip.ws.erp.ConfigurationHolder.*;

/**
 * Author: Upatov Egor <br>
 * Date: 11.01.2017, 23:46 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Configuration
public class SchedulerConfig {
    private static final Logger log = LoggerFactory.getLogger("CONFIG");

    @Bean(name="unregularAllocateJobDetail")
    public JobDetail unregularAllocateJob(){
        final JobDetailImpl result = new JobDetailImpl();
        result.setJobClass(UnregularAllocateJob.class);
        result.setKey(JobKey.jobKey("allocate", "unregular"));
        log.info("Create JobDetail[{}]", result.getKey());
        return result;
    }

    @Bean(name="unregularReAllocateJobDetail")
    public JobDetail unregularReAllocateJob(){
        final JobDetailImpl result = new JobDetailImpl();
        result.setJobClass(UnregularReAllocateJob.class);
        result.setKey(JobKey.jobKey("reAllocate", "unregular"));
        log.info("Create JobDetail[{}]", result.getKey());
        return result;
    }

    @Bean(name="unregularAllocateResultJobDetail")
    public JobDetail unregularAllocateResultJob(){
        final JobDetailImpl result = new JobDetailImpl();
        result.setJobClass(UnregularAllocateResultJob.class);
        result.setKey(JobKey.jobKey("allocateResult", "unregular"));
        log.info("Create JobDetail[{}]", result.getKey());
        return result;
    }


    @Bean(name="unregularAllocateResultTrigger")
    public CronTriggerFactoryBean unregularAllocateResultTrigger(
            final ConfigurationHolder cfg,
            @Qualifier("unregularAllocateResultJobDetail") final JobDetail jobDetail
    ) throws ParseException {
        final TriggerKey triggerKey = TriggerKey.triggerKey(jobDetail.getKey().getName(), jobDetail.getKey().getGroup());
        final String cronString = cfg.get(CFG_KEY_SCHEDULE_UNREGULAR_ALLOCATERESULT);
        final CronTriggerFactoryBean result = new CronTriggerFactoryBean();
        result.setJobDetail(jobDetail);
        result.setName(triggerKey.getName());
        result.setGroup(triggerKey.getGroup());
        result.setCronExpression(cronString);
        log.info("Create Trigger[{}] with cron='{}' for Job[{}]", triggerKey, cronString, jobDetail.getKey());
        return result;
    }

    @Bean(name="unregularAllocateTrigger")
    public CronTriggerFactoryBean unregularAllocateTrigger(
            final ConfigurationHolder cfg,
            @Qualifier("unregularAllocateJobDetail") final JobDetail jobDetail
    ) throws ParseException {
        final TriggerKey triggerKey = TriggerKey.triggerKey(jobDetail.getKey().getName(), jobDetail.getKey().getGroup());
        final String cronString = cfg.get(CFG_KEY_SCHEDULE_UNREGULAR_ALLOCATE);
        final CronTriggerFactoryBean result = new CronTriggerFactoryBean();
        result.setJobDetail(jobDetail);
        result.setName(triggerKey.getName());
        result.setGroup(triggerKey.getGroup());
        result.setCronExpression(cronString);
        log.info("Create Trigger[{}] with cron='{}' for Job[{}]", triggerKey, cronString, jobDetail.getKey());
        return result;
    }

    @Bean(name="unregularReAllocateTrigger")
    public CronTriggerFactoryBean unregularReAllocateTrigger(
            final ConfigurationHolder cfg,
            @Qualifier("unregularReAllocateJobDetail") final JobDetail jobDetail
    ) throws ParseException {
        final TriggerKey triggerKey = TriggerKey.triggerKey(jobDetail.getKey().getName(), jobDetail.getKey().getGroup());
        final String cronString = cfg.get(CFG_KEY_SCHEDULE_UNREGULAR_REALLOCATE);
        final CronTriggerFactoryBean result = new CronTriggerFactoryBean();
        result.setJobDetail(jobDetail);
        result.setName(triggerKey.getName());
        result.setGroup(triggerKey.getGroup());
        result.setCronExpression(cronString);
        log.info("Create Trigger[{}] with cron='{}' for Job[{}]", triggerKey, cronString, jobDetail.getKey());
        return result;
    }

    @Bean(name="autowiringJobFactory")
    public JobFactory autowiringSpringBeanJobFactory(final ApplicationContext context){
        final AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(context);
        return jobFactory;
    }


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(
            final Trigger[] triggers,
            final JobFactory jobFactory
    ) {
        final SchedulerFactoryBean result = new SchedulerFactoryBean();
        result.setJobFactory(jobFactory);
        final Properties props  =  new Properties();
        props.setProperty(StdSchedulerFactory.PROP_SCHED_SKIP_UPDATE_CHECK,"true");
        result.setQuartzProperties(props);
        result.setTriggers(triggers);
        log.info("Create ScheduleFactory[@{}] with Triggers: {}", Integer.toHexString(result.hashCode()), triggers);
        return result;
    }


}
