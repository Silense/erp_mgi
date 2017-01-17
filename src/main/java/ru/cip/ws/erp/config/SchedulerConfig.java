package ru.cip.ws.erp.config;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
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
import ru.cip.ws.erp.jpa.dao.SystemSettingsDaoImpl;
import ru.cip.ws.erp.jpa.entity.RsysSystemService;
import ru.cip.ws.erp.quartz.*;

import java.text.ParseException;
import java.util.*;

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

    @Bean(name = "unregularAllocateJobWrapper")
    public JobWrapper unregularAllocateJob(final ConfigurationHolder cfg, SystemSettingsDaoImpl settingsDao) throws ParseException {
        final RsysSystemService serviceSetting = settingsDao.getServiceSetting(cfg.get(CFG_KEY_APP_ID));
        final JobWrapper result = new JobWrapper(
                "allocate",
                "unregular",
                UnregularAllocateJob.class,
                serviceSetting.getSchedule(),
                null
        );
        log.info("Create {}", result);
        return result;
    }

    @Bean(name = "unregularReAllocateJobWrapper")
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

    @Bean(name = "unregularAllocateResultJobWrapper")
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

    @Bean(name = "autowiringJobFactory")
    public JobFactory autowiringSpringBeanJobFactory(final ApplicationContext context) {
        final AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(context);
        return jobFactory;
    }


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(
            final Set<JobWrapper> jobWrappers,
            final JobFactory jobFactory
    ) throws SchedulerException {
        final SchedulerFactoryBean result = new SchedulerFactoryBean();
        result.setJobFactory(jobFactory);
        final Properties props = new Properties();
        props.setProperty(StdSchedulerFactory.PROP_SCHED_SKIP_UPDATE_CHECK, "true");
        result.setQuartzProperties(props);
        final List<Trigger> triggers = new ArrayList<>(jobWrappers.size());
        final List<JobDetail> jobDetails = new ArrayList<>(jobWrappers.size());
        for (JobWrapper wrapper : jobWrappers) {
            jobDetails.add(wrapper.getJobDetail());
            if(wrapper.getCronTrigger() != null){
                triggers.add(wrapper.getCronTrigger());
            }
        }
        result.setTriggers(triggers.toArray(new Trigger[triggers.size()]));
        result.setJobDetails(jobDetails.toArray(new JobDetail[jobDetails.size()]));
        log.info("Create ScheduleFactory[@{}] with Triggers: {}", Integer.toHexString(result.hashCode()), triggers);
        return result;
    }


}
