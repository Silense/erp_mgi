package ru.cip.ws.erp.quartz;

import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;

/**
 * Author: Upatov Egor <br>
 * Date: 17.01.2017, 8:29 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
public class JobWrapper {
    private final String name;
    private final String group;
    private CronTrigger trigger;
    private final JobDetail jobDetail;
    private final String cfgKey;

    public JobWrapper(String name, String group, Class<? extends Job> jobClazz, String cron, String cfgKey) throws ParseException {
        this.name = name;
        this.group = group;
        this.cfgKey = cfgKey;
        final JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setJobClass(jobClazz);
        jobDetail.setKey(JobKey.jobKey(name, group));
        jobDetail.setDurability(true);
        this.jobDetail = jobDetail;
        if(StringUtils.isNotEmpty(cron)){
           createNewCronTrigger(cron);
        }
    }

    public Trigger createNewCronTrigger(String cronExpression) throws ParseException {
        final CronTriggerImpl trigger = new CronTriggerImpl();
        trigger.setGroup(group);
        trigger.setName(name);
        trigger.setJobKey(jobDetail.getKey());
        trigger.getJobDataMap().put("jobDetail", this.jobDetail);
        trigger.setCronExpression(cronExpression);
        this.trigger = trigger;
        return trigger;
    }

    public JobKey getJobKey(){
       return jobDetail.getKey();
    }

    public TriggerKey getTriggerKey(){
        return TriggerKey.triggerKey(name, group);
    }

    public JobDetail getJobDetail(){
        return jobDetail;
    }

    public CronTrigger getCronTrigger(){
        return trigger;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JobWrapper[").append(group).append('.').append(name);
        sb.append("]{ JobDetail[@").append(Integer.toHexString(jobDetail.hashCode()));
        sb.append("], Trigger[");
        if(trigger == null){
          sb.append("null");
        } else {
            sb.append('@').append(Integer.toHexString(trigger.hashCode())).append(" with '").append(trigger.getCronExpression()).append('\'');
        }
        sb.append("], cfgKey='").append(cfgKey).append("'}");
        return sb.toString();
    }

    public String getCfgKey() {
        return cfgKey;
    }


}
