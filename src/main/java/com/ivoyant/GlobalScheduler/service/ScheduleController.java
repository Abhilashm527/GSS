package com.ivoyant.GlobalScheduler.service;

import com.ivoyant.GlobalScheduler.Model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class ScheduleController implements ScheduleIn {


    @Autowired
    private ScheduleImpl scheduleImpl;

    @Override
    public ResponseEntity createSchedule(Schedule schedule) throws SQLException {
        Schedule schedule1 = scheduleImpl.create(schedule);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body("Schedule has been created");
    }

    @Override
    public ResponseEntity getScheduleById(int id) throws SQLException {
        Schedule schedule = scheduleImpl.getSchedule(id);
        if (schedule == null)
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("The Schedule is not found");
        return ResponseEntity.ok(schedule);
    }

    @Override
    public ResponseEntity updateScheduleById(Schedule schedule) throws SQLException {
        Schedule schedule1 = scheduleImpl.updateSchedule(schedule);
        return ResponseEntity.ok(schedule1);
    }

    @Override
    public ResponseEntity deleteScheduleById(int id) {
        boolean isRemoved = scheduleImpl.deleteSchedule(id);
        if (isRemoved)
            return ResponseEntity.ok("The schedule has been removed");
        return ResponseEntity.ok("The Schedule is not Found");
    }

    @Override
    public ResponseEntity getAllSchedule() {
        List<Schedule> scheduleList = scheduleImpl.getAllSchedules();
        if (scheduleList == null)
            return ResponseEntity.ok("No schedules");
        return ResponseEntity.ok(scheduleList);
    }
}
