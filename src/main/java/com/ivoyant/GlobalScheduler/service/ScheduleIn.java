package com.ivoyant.GlobalScheduler.service;

import com.ivoyant.GlobalScheduler.Model.Schedule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("restservices/globalScheduler/api")
public interface ScheduleIn {

    @PostMapping("/createSchedule")
    public ResponseEntity createSchedule(@RequestBody Schedule schedule) throws SQLException;

    @GetMapping("/getSchedule/{id}")
    public ResponseEntity getScheduleById(@PathVariable int id) throws SQLException;

    @PutMapping("/updateSchedule")
    public ResponseEntity updateScheduleById(@RequestBody Schedule schedule) throws SQLException;

    @DeleteMapping("/deleteSchedule/{id}")
    public ResponseEntity deleteScheduleById(@PathVariable int id);

    @GetMapping("/getAllSchedule")
    public ResponseEntity getAllSchedule();


    @GetMapping("/getScheduleHistory/{id}")
    public ResponseEntity getScheduleHistory(@PathVariable int id) throws SQLException;

}
