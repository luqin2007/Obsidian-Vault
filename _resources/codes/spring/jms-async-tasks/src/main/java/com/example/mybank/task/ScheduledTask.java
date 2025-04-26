package com.example.mybank.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    @Scheduled(fixedDelay = 500)
    public void runTask() {
        System.out.println("Running scheduled task");
    }

    @Async
    public void asyncTask() {
        System.out.println("Running async task");
    }
}
