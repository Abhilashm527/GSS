package com.ivoyant.GlobalScheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class GlobalSchedulerApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/kolkata"));
        SpringApplication.run(GlobalSchedulerApplication.class, args);
    }

}
