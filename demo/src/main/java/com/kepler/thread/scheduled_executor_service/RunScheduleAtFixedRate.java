package com.kepler.thread.scheduled_executor_service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RunScheduleAtFixedRate {
    public static void main(String[] args){
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        System.out.println("callable start :" + System.currentTimeMillis());
        executor.scheduleAtFixedRate(new MyRunnable(), 1, 2, TimeUnit.SECONDS);
        System.out.println("callable end :" + System.currentTimeMillis());
    }
}