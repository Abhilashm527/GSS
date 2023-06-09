package com.ivoyant.GlobalScheduler.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;


@Getter
@Setter
public class Schedule {

    private int scheduleId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("target")
    private String target;
    @JsonProperty("active")
    private boolean active = false;
    @JsonProperty("targetType")
    private String targetType;
    @JsonProperty("cron_expression")
    private String cron_expression;
    @JsonProperty("zoneId")
    private String zoneId;
    @JsonProperty("createdTime")
    private Date createdTime;
    @JsonProperty("lastRun")
    private Date lastRun;
    @JsonProperty("nextRun")
    private Date nextRun;
    @JsonProperty("state")
    private ScheduleState state;

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getCron_expression() {
        return cron_expression;
    }

    public void setCron_expression(String cron_expression) {
        this.cron_expression = cron_expression;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastRun() {
        return lastRun;
    }

    public void setLastRun(Timestamp lastRun) {
        this.lastRun = lastRun;
    }

    public Date getNextRun() {
        return nextRun;
    }

    public void setNextRun(Timestamp nextRun) {
        this.nextRun = nextRun;
    }

    public ScheduleState getState() {
        return state;
    }

    public void setState(ScheduleState state) {
        this.state = state;
    }
}
