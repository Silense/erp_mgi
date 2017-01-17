package ru.cip.ws.erp.servlet;

import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cip.ws.erp.ConfigurationHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static ru.cip.ws.erp.ConfigurationHolder.CFG_KEY_SCHEDULE_UNREGULAR_ALLOCATERESULT;
import static ru.cip.ws.erp.ConfigurationHolder.TEXT_PLAIN_UTF8;

/**
 * Author: Upatov Egor <br>
 * Date: 11.01.2017, 15:36 <br>
 * Description: Сервис для настройки выгрузок внеплановых проверок по расписанию
 */
@RestController
@RequestMapping("/schedule/unregular.allocateResult")
public class ScheduleUnregularAllocateResultRest {
    private static final Logger log = LoggerFactory.getLogger("SCHEDULE");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Autowired
    private Scheduler scheduler;

    @Autowired
    @Qualifier("unregularAllocateResultTrigger")
    private Trigger trigger;

    @Autowired
    @Qualifier("unregularAllocateResultJobDetail")
    private JobDetail job;

    @Autowired
    private ConfigurationHolder cfg;


    @RequestMapping(value = "/update", produces = TEXT_PLAIN_UTF8)
    public ResponseEntity<String> updateSchedule(
            @RequestParam(value = "second", required = false) final String second,
            @RequestParam(value = "minute", required = false) final String minute,
            @RequestParam(value = "hour", required = false) final String hour,
            @RequestParam(value = "day", required = false) final String day,
            @RequestParam(value = "month", required = false) final String month,
            @RequestParam(value = "year", required = false) final String year
    ) {
        log.info("Start update [{}] with [year='{}' month='{}' day='{}' hour='{}' minute='{}' second='{}']",
                job.getKey(),
                year,
                month,
                day,
                hour,
                minute,
                second
        );
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
        cronExpression.append("? ");
        if (StringUtils.isNotEmpty(year)) {
            cronExpression.append(year.trim()).append(" ");
        } else {
            cronExpression.append("*");
        }
        final String result = setScheduleExpression(cronExpression.toString());
        log.info("End update [{}]. Result={} ", job.getKey(), result);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/update", produces = TEXT_PLAIN_UTF8, params = {"cron"})
    public ResponseEntity<String> updateSchedule(
            @RequestParam("cron") final String cronExpression
    ) {
        log.info("Start update [{}] with cron='{}'", job.getKey(), cronExpression);
        final String result = setScheduleExpression(cronExpression);
        log.info("End update [{}]. Result={} ", job.getKey(), result);
        return ResponseEntity.ok(result);
    }

    private String setScheduleExpression(final String cronExpression) {
        try {
            CronExpression.validateExpression(cronExpression);
        } catch (ParseException e) {
            log.warn("Cron expression ['{}'] is invalid: {}", cronExpression, e.getMessage());
            return "Введите корректные параметры планировщика: <br/> " +
                    "для minute: 0-59, *, 0/10, 1,2,3 <br/> " +
                    "для hour: 0-23, *, 0/2, 1,2,3 <br/> " +
                    "для day: 0-31, *, 0/5, 1,2,3 <br/>" +
                    "Текущий конфиг не корректен:" + e.getMessage();
        }
        final TriggerKey triggerKey = trigger.getKey();
        final Trigger newTrigger = ((TriggerBuilder<CronTrigger>) trigger.getTriggerBuilder())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
        try {
            if (scheduler.isShutdown()) {
                scheduler.start();
            }
            scheduler.rescheduleJob(triggerKey, newTrigger);
            cfg.set(CFG_KEY_SCHEDULE_UNREGULAR_ALLOCATERESULT, cronExpression);

            return "Настройки расписания отправки внеплановых проверок изменены на '" + cronExpression + "'";
        } catch (SchedulerException e) {
            log.error("Exception while rescheduling Trigger[{}]", triggerKey, e);
            return String.format("Ошибка при замене расписания для %s: %s", triggerKey.toString(), e.getMessage());
        }
    }

    @RequestMapping(value = "/status", produces = TEXT_PLAIN_UTF8)
    public ResponseEntity<String> status() throws SchedulerException {
        log.info("Start status [{}]", job.getKey());
        final StringBuilder result = new StringBuilder();
        if (scheduler.isStarted()) {
            result.append("Планировщик запущен <br/>");
        } else {
            result.append("Планировщик НЕ запущен <br/>");
        }
        if (scheduler.isInStandbyMode()) {
            result.append("Планировщик приостановлен (standBy) <br/>");
        }
        final JobDetail jobDetail = scheduler.getJobDetail(job.getKey());
        if (jobDetail != null) {
            result.append("Искомое задание найдено JobDetail[").append(job.getKey())
                    .append("]<br/>");
            final List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(job.getKey());
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
            result.append("Искомое задание НЕ найдено JobDetail[").append(job.getKey()).append("]<br/>");
        }
        log.info("End status [{}]. Result={} ", job.getKey(), result);
        return ResponseEntity.ok(result.toString());
    }

    @RequestMapping(value = "/suspend", produces = TEXT_PLAIN_UTF8)
    public ResponseEntity<String> suspend() throws SchedulerException {
        log.info("Start suspend [{}]", job.getKey());
        scheduler.pauseJob(job.getKey());
        final Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
        log.info("End suspend [{}]. Result={} ", job.getKey(), triggerState);
        return ResponseEntity.ok(triggerState.toString());
    }

    @RequestMapping(value = "/resume", produces = TEXT_PLAIN_UTF8)
    public ResponseEntity<String> resume() throws SchedulerException {
        log.info("Start resume [{}]", job.getKey());
        scheduler.resumeJob(job.getKey());
        final Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
        log.info("End resume [{}]. Result={} ", job.getKey(), triggerState);
        return ResponseEntity.ok(triggerState.toString());
    }

    @RequestMapping(value = "/fire", produces = TEXT_PLAIN_UTF8)
    public ResponseEntity<String> fire() throws SchedulerException {
        log.info("Start fire [{}]", job.getKey());
        scheduler.triggerJob(job.getKey());
        log.info("End fire [{}]. Result={} ", job.getKey(), "OK");
        return ResponseEntity.ok("OK");
    }
}
