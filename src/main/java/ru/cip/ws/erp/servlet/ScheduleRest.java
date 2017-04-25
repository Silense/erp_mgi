package ru.cip.ws.erp.servlet;

import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cip.ws.erp.ConfigurationHolder;
import ru.cip.ws.erp.jpa.dao.SystemSettingsDaoImpl;
import ru.cip.ws.erp.quartz.JobWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static ru.cip.ws.erp.ConfigurationHolder.*;

/**
 * Author: Upatov Egor <br>
 * Date: 11.01.2017, 15:36 <br>
 * Description: Сервис для настройки выгрузок внеплановых проверок по расписанию
 */
@RestController
@RequestMapping("/schedule/{group}/{name}")
public class ScheduleRest {
    private static final Logger log = LoggerFactory.getLogger("SCHEDULE");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private Scheduler scheduler;


    @Autowired
    private Set<JobWrapper> jobWrappers;

    @Autowired
    private ConfigurationHolder cfg;

    @Autowired
    private SystemSettingsDaoImpl settingsDao;


    @RequestMapping(value = "/update", produces = TEXT_PLAIN_UTF8)
    public ResponseEntity<String> updateSchedule(
            @PathVariable("group") final String group,
            @PathVariable("name") final String name,
            @RequestParam(value = "second", required = false) final String second,
            @RequestParam(value = "minute", required = false) final String minute,
            @RequestParam(value = "hour", required = false) final String hour,
            @RequestParam(value = "day", required = false) final String day,
            @RequestParam(value = "month", required = false) final String month,
            @RequestParam(value = "year", required = false) final String year
    ) {
        log.info("Start update [{}.{}] with [year='{}' month='{}' day='{}' hour='{}' minute='{}' second='{}']",
                group, name, year, month, day, hour, minute, second
        );
        final JobWrapper wrapper = getJobWrapper(group, name);
        if(wrapper == null){
            log.warn("End update [{}.{}]: No such JobWrapper", group, name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Нет задачи с таким ключом");
        }
        final StringBuilder cronExpression = new StringBuilder();
        if (StringUtils.isNotEmpty(second)) {
            cronExpression.append(second.trim()).append(" ");
        } else {
            cronExpression.append("0 ");
        }
        if (StringUtils.isNotEmpty(minute)) {
            cronExpression.append(minute.trim()).append(" ");
        } else {
            cronExpression.append("0 ");
        }
        if (StringUtils.isNotEmpty(hour)) {
            cronExpression.append(hour.trim()).append(" ");
        } else {
            cronExpression.append("0 ");
        }
        if (StringUtils.isNotEmpty(day)) {
            cronExpression.append(day.trim()).append(" ");
        } else {
            cronExpression.append("* ");
        }
        if (StringUtils.isNotEmpty(month)) {
            cronExpression.append(month.trim()).append(" ");
        } else {
            cronExpression.append("* ");
        }
        cronExpression.append("?");
//        if (StringUtils.isNotEmpty(year)) {
//            cronExpression.append(year.trim());
//        }
        final String result = setScheduleExpression(wrapper, cronExpression.toString());
        log.info("End update [{}]. Result={} ", wrapper.getJobKey(), result);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/update", produces = TEXT_PLAIN_UTF8, params = {"cron"})
    public ResponseEntity<String> updateSchedule(
            @PathVariable("group") final String group,
            @PathVariable("name") final String name,
            @RequestParam("cron") final String cronExpression
    ) {
        log.info("Start update [{}.{}] with cron='{}'", group, name, cronExpression);
        final JobWrapper wrapper = getJobWrapper(group, name);
        if(wrapper == null){
            log.warn("End update [{}.{}]: No such JobWrapper", group, name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Нет задачи с таким ключом");
        }
        final String result = setScheduleExpression(wrapper, cronExpression);
        log.info("End update [{}]. Result={} ", wrapper.getJobKey(), result);
        return ResponseEntity.ok(result);
    }

    private JobWrapper getJobWrapper(String group, String name) {
        final JobKey toFind = JobKey.jobKey(name, group);
        for (JobWrapper jobWrapper : jobWrappers) {
            if(Objects.equals(toFind, jobWrapper.getJobKey())) {
                return jobWrapper;
            }
        }
        return null;
    }

    private String setScheduleExpression(JobWrapper wrapper, final String cronExpression) {
        try {
            CronExpression.validateExpression(cronExpression);
            if (scheduler.isShutdown()) {
                scheduler.start();
            }
            scheduler.deleteJob(wrapper.getJobKey());
            scheduler.scheduleJob(wrapper.getJobDetail(), wrapper.createNewCronTrigger(cronExpression));
            if(StringUtils.isNotEmpty(wrapper.getCfgKey())){
                settingsDao.setNewStringValue(cfg.getAppId(), wrapper.getCfgKey(), cronExpression);
            }  else {
                settingsDao.setServiceSettingSchedule(cfg.getAppId(), cronExpression);
            }
            return "Настройки расписания отправки внеплановых проверок изменены на '" + cronExpression + "'";
        }  catch (ParseException e) {
            log.warn("Cron expression ['{}'] is invalid: {}", cronExpression, e.getMessage());
            return "Введите корректные параметры планировщика: <br/> " +
                    "для minute: 0-59, *, 0/10, 1,2,3 <br/> " +
                    "для hour: 0-23, *, 0/2, 1,2,3 <br/> " +
                    "для day: 0-31, *, 0/5, 1,2,3 <br/>" +
                    "Текущий конфиг не корректен:" + e.getMessage();
        }   catch (SchedulerException e) {
            log.error("Exception while rescheduling Trigger[{}]", wrapper.getTriggerKey(), e);
            return String.format("Ошибка при замене расписания для %s: %s", wrapper.getTriggerKey().toString(), e.getMessage());
        }
    }

    @RequestMapping(value = "/status", produces = TEXT_PLAIN_UTF8)
    public ResponseEntity<String> status(
            @PathVariable("group") final String group,
            @PathVariable("name") final String name
    ) throws SchedulerException {
        log.info("Start status [{}.{}]", group, name);
        final JobWrapper wrapper = getJobWrapper(group, name);
        if(wrapper == null){
            log.warn("End status [{}.{}]: No such JobWrapper", group, name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Нет задачи с таким ключом");
        }
        final StringBuilder result = new StringBuilder();
        if (scheduler.isStarted()) {
            result.append("Планировщик запущен <br/>");
        } else {
            result.append("Планировщик НЕ запущен <br/>");
        }
        if (scheduler.isInStandbyMode()) {
            result.append("Планировщик приостановлен (standBy) <br/>");
        }
        final JobDetail jobDetail = scheduler.getJobDetail(wrapper.getJobKey());
        if (jobDetail != null) {
            result.append("Искомое задание найдено JobDetail[").append(wrapper.getJobKey())
                    .append("]. Последняя дата запуска '")
                    .append(sdf.format(settingsDao.getDate(cfg.getAppId(), CFG_KEY_SCHEDULE_UNREGULAR_ALLOCATE_LAST_FIRE_DATE)))
                    .append("'<br/>");
            final List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(wrapper.getJobKey());
            result.append("Задание имеет ").append(triggersOfJob.size()).append(" триггер(а/ов) для запуска<br/>");
            for (Trigger item : triggersOfJob) {
                final Trigger.TriggerState itemState = scheduler.getTriggerState(item.getKey());
                result.append(item.getClass().getSimpleName())
                        .append("[").append(item.getKey()).append("]: состояние=").append(itemState)
                        .append(", предыдущий запуск='").append(item.getPreviousFireTime() == null ? "НЕТ" : sdf.format(item.getPreviousFireTime()))
                        .append("', следующий запуск='").append(item.getNextFireTime() == null ? "НЕТ" : sdf.format(item.getNextFireTime()))
                        .append("'<br/>");
            }
        } else {
            result.append("Искомое задание НЕ найдено JobDetail[").append(wrapper.getJobKey()).append("]<br/>");
        }
        log.info("End status [{}]. Result={} ",wrapper.getJobKey(), result);
        return ResponseEntity.ok().header("loading", String.valueOf(isJobRunning(wrapper.getJobKey()))).body(result.toString());
    }

    private boolean isJobRunning(JobKey key) throws SchedulerException {
        for (JobExecutionContext jobCtx : scheduler.getCurrentlyExecutingJobs()) {
            if (Objects.equals(jobCtx.getJobDetail().getKey(), key)) {
                return true;
            }
        }
        return false;
    }


    @RequestMapping(value = "/suspend", produces = TEXT_PLAIN_UTF8)
    public ResponseEntity<String> suspend(
            @PathVariable("group") final String group,
            @PathVariable("name") final String name
    ) throws SchedulerException {
        log.info("Start suspend [{}.{}]", group, name);
        final JobWrapper wrapper = getJobWrapper(group, name);
        if(wrapper == null){
            log.warn("End status [{}.{}]: No such JobWrapper", group, name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Нет задачи с таким ключом");
        }
        scheduler.pauseJob(wrapper.getJobKey());
        final Trigger.TriggerState triggerState = scheduler.getTriggerState(wrapper.getTriggerKey());
        log.info("End suspend [{}]. Result={} ",wrapper.getJobKey(), triggerState);
        return ResponseEntity.ok(triggerState.toString());
    }

    @RequestMapping(value = "/resume", produces = TEXT_PLAIN_UTF8)
    public ResponseEntity<String> resume(
            @PathVariable("group") final String group,
            @PathVariable("name") final String name
    ) throws SchedulerException {
        log.info("Start resume [{}.{}]", group, name);
        final JobWrapper wrapper = getJobWrapper(group, name);
        if(wrapper == null){
            log.warn("End status [{}.{}]: No such JobWrapper", group, name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Нет задачи с таким ключом");
        }
        scheduler.resumeJob(wrapper.getJobKey());
        final Trigger.TriggerState triggerState = scheduler.getTriggerState(wrapper.getTriggerKey());
        log.info("End resume [{}]. Result={} ", wrapper.getJobKey(), triggerState);
        return ResponseEntity.ok(triggerState.toString());
    }

    @RequestMapping(value = "/fire", produces = TEXT_PLAIN_UTF8)
    public ResponseEntity<String> fire(
            @PathVariable("group") final String group,
            @PathVariable("name") final String name
    ) throws SchedulerException {
        log.info("Start fire [{}.{}]", group, name);
        final JobWrapper wrapper = getJobWrapper(group, name);
        if(wrapper == null){
            log.warn("End status [{}.{}]: No such JobWrapper", group, name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Нет задачи с таким ключом");
        }
        scheduler.triggerJob(wrapper.getJobKey());
        scheduler.resumeJob(wrapper.getJobKey());
        log.info("End fire [{}]. Result={} ", wrapper.getJobKey(), "OK");
        return ResponseEntity.ok("OK");
    }
}
