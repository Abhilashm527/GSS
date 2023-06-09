package com.ivoyant.GlobalScheduler.service;

import com.ivoyant.GlobalScheduler.Model.Schedule;
import com.ivoyant.GlobalScheduler.Model.ScheduleState;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class ScheduleImpl {

    private final JdbcTemplate jdbcTemplate;
    private static final String insertSQl = "INSERT INTO public.schedule(\n" +
            "\tscheduleid, name, description, target, active, targettype, cron_expression, zoneid, createdtime, lastrun, nextrun, state)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String updateSQl = "UPDATE public.schedule\n" +
            "\tSET scheduleid=?, name=?, description=?, target=?, active=?, targettype=?, cron_expression=?, zoneid=?, createdtime=?, lastrun=?, nextrun=?, state=?\n" +
            "\tWHERE scheduleid=?";
    private static final String getAllScheduleSQl = "select * from public.schedule";
    private static final String getScheduleSQl = "select * from public.schedule where scheduleid = ?";
    private static final String deleteScheduleSQl = "delete from public.schedule where scheduleid = ?";

    public ScheduleImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Connection jdbcTemplateConnection;

    @PostConstruct
    public void prepareQueries() throws SQLException {
        jdbcTemplateConnection = jdbcTemplate.getDataSource().getConnection();
    }


    public Schedule create(Schedule schedule) throws SQLException {
        PreparedStatement insertStmt = jdbcTemplateConnection.prepareStatement(insertSQl);
        java.util.Date currentDate = new java.util.Date();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());

        ZoneId zoneId = ZoneId.of(schedule.getZoneId());
        ZonedDateTime zonedDateTime = currentTimestamp.toInstant().atZone(zoneId);
        OffsetDateTime offsetDateTime = zonedDateTime.toOffsetDateTime();

        int scheduleId = generateRandomNumber();
        insertStmt.setInt(1, scheduleId);
        insertStmt.setString(2, schedule.getName());
        insertStmt.setString(3, schedule.getName());
        insertStmt.setString(4, schedule.getTarget());
        insertStmt.setBoolean(5, schedule.isActive());
        insertStmt.setString(6, schedule.getTargetType());
        insertStmt.setString(7, schedule.getCron_expression());
        insertStmt.setString(8, schedule.getZoneId());
        insertStmt.setTimestamp(9, Timestamp.valueOf(offsetDateTime.toLocalDateTime()));
        insertStmt.setTimestamp(10, null);
        insertStmt.setTimestamp(11, null);
        insertStmt.setString(12, ScheduleState.CREATED.toString());
        insertStmt.execute();
        return schedule;
    }


    public Schedule getSchedule(int id) throws SQLException {
        PreparedStatement getScheduleByid = jdbcTemplateConnection.prepareStatement(getScheduleSQl);
        getScheduleByid.setInt(1, id);
        ResultSet resultSet = getScheduleByid.executeQuery();
        Schedule schedule = null;
        while (resultSet.next()) {
            schedule = new Schedule();
            schedule.setScheduleId(resultSet.getInt("scheduleId"));
            schedule.setName(resultSet.getString("name"));
            schedule.setDescription(resultSet.getString("description"));
            schedule.setTarget(resultSet.getString("target"));
            schedule.setActive(resultSet.getBoolean("active"));
            schedule.setTargetType(resultSet.getString("targetType"));
            schedule.setCron_expression(resultSet.getString("cron_expression"));
            schedule.setZoneId(resultSet.getString("zoneId"));
            schedule.setCreatedTime(resultSet.getTimestamp("createdTime"));
            schedule.setLastRun(resultSet.getTimestamp("lastRun"));
            schedule.setNextRun(resultSet.getTimestamp("nextRun"));
            schedule.setState(ScheduleState.valueOf(resultSet.getString("state")));
        }
        if (schedule == null)
            return null;
        return schedule;
    }

    public Schedule updateSchedule(Schedule schedule) throws SQLException {
        PreparedStatement updateStmt = jdbcTemplateConnection.prepareStatement(updateSQl);
        updateStmt.setInt(1, schedule.getScheduleId());
        // Set other parameters for the update query

        PreparedStatement getScheduleByid = jdbcTemplateConnection.prepareStatement(getScheduleSQl);
        getScheduleByid.setInt(1, schedule.getScheduleId());
        ResultSet resultSet = getScheduleByid.executeQuery();
        if (resultSet.next()) {
            updateStmt.setString(2, schedule.getName());
            updateStmt.setString(3, schedule.getDescription());
            updateStmt.setString(4, schedule.getTarget());
            updateStmt.setBoolean(5, schedule.isActive());
            updateStmt.setString(6, schedule.getTargetType());
            updateStmt.setString(7, schedule.getCron_expression());
            updateStmt.setString(8, schedule.getZoneId());
            updateStmt.setTimestamp(9, (Timestamp) schedule.getCreatedTime());
            updateStmt.setTimestamp(10, (Timestamp) schedule.getLastRun());
            updateStmt.setTimestamp(11, (Timestamp) schedule.getNextRun());
            updateStmt.setString(12, schedule.getState().toString());
            updateStmt.setInt(13, schedule.getScheduleId()); // Set the ID for the WHERE clause of the update query
            updateStmt.executeUpdate();
        }

        // Close the statements and result set
        updateStmt.close();
        getScheduleByid.close();
        resultSet.close();

        return schedule;
    }


    public boolean deleteSchedule(int id) {
        try {
            PreparedStatement deleteScheduleById = jdbcTemplateConnection.prepareStatement(deleteScheduleSQl);
            deleteScheduleById.setInt(1, id);
            int rows = deleteScheduleById.executeUpdate();
            if (rows <= 0)
                return false;
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Schedule> getAllSchedules() {
        try {
            PreparedStatement getAllSchedules = jdbcTemplateConnection.prepareStatement(getAllScheduleSQl);
            ResultSet resultSet = getAllSchedules.executeQuery();
            List<Schedule> scheduleList = new ArrayList<>();
            while (resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setScheduleId(resultSet.getInt("scheduleId"));
                schedule.setName(resultSet.getString("name"));
                schedule.setDescription(resultSet.getString("description"));
                schedule.setTarget(resultSet.getString("target"));
                schedule.setActive(resultSet.getBoolean("active"));
                schedule.setTargetType(resultSet.getString("targetType"));
                schedule.setCron_expression(resultSet.getString("cron_expression"));
                schedule.setZoneId(resultSet.getString("zoneId"));
                schedule.setCreatedTime(resultSet.getTimestamp("createdTime"));
                schedule.setLastRun(resultSet.getTimestamp("lastRun"));
                schedule.setNextRun(resultSet.getTimestamp("nextRun"));
                schedule.setState(ScheduleState.valueOf(resultSet.getString("state")));
                scheduleList.add(schedule);
            }
            return scheduleList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static int generateRandomNumber() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        return random.nextInt(max - min + 1) + min;
    }
}
