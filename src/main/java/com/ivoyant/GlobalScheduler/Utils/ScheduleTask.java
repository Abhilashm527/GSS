package com.ivoyant.GlobalScheduler.Utils;

import com.ivoyant.GlobalScheduler.Model.Schedule;
import com.ivoyant.GlobalScheduler.service.ScheduleImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.quartz.CronExpression;

import java.util.List;

@Component
@EnableScheduling
public class ScheduleTask {

    private final TaskScheduler taskScheduler;

    @Autowired
    public ScheduleTask(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Autowired
    private ScheduleImpl scheduleService;

    @Autowired
    private ZoneFormatter zoneFormatter;


    @Scheduled(fixedRate = 10000)
    public void executeTask() throws SQLException {
        List<Schedule> scheduleList = scheduleService.getAllSchedules();
        for (Schedule schedule : scheduleList) {
            if (schedule.isActive()) {
                String expression = schedule.getCron_expression();
                try {
                    CronExpression cron = new CronExpression(expression);
                    Date nextRun = cron.getNextValidTimeAfter(new java.util.Date());
                    Timestamp next = zoneFormatter.formatToZone(nextRun, schedule.getZoneId());
                    System.out.println("Next run: " + next);
                    schedule.setNextRun(next);
                } catch (Exception e) {
                    System.out.println("Invalid Cron expression");
                }
                scheduleService.updateSchedule(schedule);
            }

        }
        System.out.println("Executing task...");
    }
}
