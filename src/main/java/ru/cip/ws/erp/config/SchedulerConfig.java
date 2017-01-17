package ru.cip.ws.erp.config;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import ru.cip.ws.erp.ConfigurationHolder;
import ru.cip.ws.erp.quartz.*;

import java.text.ParseException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

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

    @Bean(name="unregularAllocateJobWrapper")
    public JobWrapper unregularAllocateJob(final ConfigurationHolder cfg) throws ParseException {
        final JobWrapper result = new JobWrapper(
                "allocate",
                "unregular",
                UnregularAllocateJob.class,
                cfg.get(CFG_KEY_SCHEDULE_UNREGULAR_ALLOCATE),
                CFG_KEY_SCHEDULE_UNREGULAR_ALLOCATE
        );
        log.info("Create {}", result);
        return result;
    }

    @Bean(name="unregularReAllocateJobWrapper")
    public JobWrapper unregularReAllocateJob(final ConfigurationHolder cfg) throws ParseException {
        final JobWrapper result = new JobWrapper(
                "reAllocate",
                "unregular",
                UnregularReAllocateJob.class,
                cfg.get(CFG_KEY_SCHEDULE_UNREGULAR_REALLOCATE),
                CFG_KEY_SCHEDULE_UNREGULAR_REALLOCATE
        );
        log.info("Create {}", result);
        return result;
    }

    @Bean(name="unregularAllocateResultJobWrapper")
    public JobWrapper unregularAllocateResultJob(final ConfigurationHolder cfg) throws ParseException {
        final JobWrapper result = new JobWrapper(
                "allocateResult",
                "unregular",
                UnregularAllocateResultJob.class,
                cfg.get(CFG_KEY_SCHEDULE_UNREGULAR_ALLOCATERESULT),
                CFG_KEY_SCHEDULE_UNREGULAR_ALLOCATERESULT
        );
        log.info("Create {}", result);
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
            final Set<JobWrapper> jobWrappers,
            final JobFactory jobFactory
    ) {
        final SchedulerFactoryBean result = new SchedulerFactoryBean();
        result.setJobFactory(jobFactory);
        final Properties props  =  new Properties();
        props.setProperty(StdSchedulerFactory.PROP_SCHED_SKIP_UPDATE_CHECK,"true");
        result.setQuartzProperties(props);
        final Trigger[] triggers = new Trigger[jobWrappers.size()];
        final JobDetail[] jobDetails = new JobDetail[jobWrappers.size()];
        final Iterator<JobWrapper> iterator = jobWrappers.iterator();
        int i = 0;
        while(iterator.hasNext()){
            final JobWrapper jobWrapper = iterator.next();
            jobDetails[i] = jobWrapper.getJobDetail();
            triggers[i] = jobWrapper.getCronTrigger();
            i++;
        }
        result.setTriggers(triggers);
        result.setJobDetails(jobDetails);
        log.info("Create ScheduleFactory[@{}] with Triggers: {}", Integer.toHexString(result.hashCode()), triggers);
        return result;
    }


}
