package com.ivoyant.GlobalScheduler.Utils;

import com.ivoyant.GlobalScheduler.Model.Schedule;
import com.ivoyant.GlobalScheduler.Model.ScheduleState;
import com.ivoyant.GlobalScheduler.service.ScheduleImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Component
@EnableScheduling
public class ScheduleProvider {
    @Autowired
    private ScheduleImpl scheduleService;

    @Autowired
    private RestExample restExample;

    @Autowired
    private ZoneFormatter zoneFormatter;


    @Scheduled(fixedRate = 1000)
    public void executeTask() throws SQLException {
        List<Schedule> scheduleList = scheduleService.getAllSchedules();

        for (Schedule schedule : scheduleList) {
            if (schedule.isActive()) {
                if (ScheduleState.CREATED == schedule.getState()) {
                    try {
                        Date date = schedule.getNextRun();
                        Timestamp nextRunTimestamp = new Timestamp(date.getTime());
                        Timestamp buffered = zoneFormatter.formatToZone(new Date(), schedule.getZoneId());
                        int value = buffered.compareTo(nextRunTimestamp);
                        System.out.println("int value " + value);
                        if (value >= 0) {
                            schedule.setState(ScheduleState.BUFFERED);
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid Cron expression");
                    }
                    scheduleService.updateSchedule(schedule);
                } else if (ScheduleState.BUFFERED == schedule.getState()) {
                    Date date = schedule.getNextRun();
                    Date bufferedTime = zoneFormatter.formatToZone(new Date(), schedule.getZoneId());
                    if (date.compareTo(bufferedTime) >= 0) {
                        schedule.setState(ScheduleState.RUNNING);
                        ResponseEntity<String> response = restExample.restCall(schedule.getTarget(), HttpMethod.valueOf(schedule.getTargetType()));
                        if (response.getStatusCode().is2xxSuccessful()) {
                            schedule.setLastRun(zoneFormatter.formatToZone(new Date(), schedule.getZoneId()));
                            System.out.println("Resonse" + response.getBody());
                            schedule.setState(ScheduleState.COMPLETED);
                            scheduleService.insertHistory(schedule);
                        }
                    }
                    scheduleService.updateSchedule(schedule);
                } else if (ScheduleState.FAILED == schedule.getState()) {
                    if (zoneFormatter.formatToZone(new Date(), schedule.getZoneId()).compareTo(schedule.getNextRun()) >= 0) {
                        schedule.setState(ScheduleState.CREATED);
                    }
                } else if (ScheduleState.COMPLETED == schedule.getState()) {
                    if (zoneFormatter.formatToZone(new Date(), schedule.getZoneId()).compareTo(schedule.getNextRun()) >= 0) {
                        schedule.setState(ScheduleState.CREATED);
                    }
                    scheduleService.updateSchedule(schedule);
                }
            } else {
                schedule.setNextRun(null);
                scheduleService.updateSchedule(schedule);
            }
        }
        System.out.println("Executing scheduleprovide...");
    }
}
