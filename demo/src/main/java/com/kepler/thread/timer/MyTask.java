package com.kepler.thread.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyTask extends TimerTask {

    @Override
    public void run() {
        System.out.println("task is running, time: " + System.currentTimeMillis());
    }

    public static void main(String[] args) throws InterruptedException {
        long nowTime = System.currentTimeMillis();
        System.out.println("now time: " + nowTime);
        long scheduleTime1 = (nowTime + 5000);
        long scheduleTime2 = (nowTime + 10000);
        System.out.println("schedule time: " + scheduleTime1);
        System.out.println("schedule time: " + scheduleTime2);
        MyTask task1 = new MyTask();
        MyTask task2 = new MyTask();
        Timer timer = new Timer();
        Thread.sleep(1000);
        timer.schedule(task1, new Date(scheduleTime1));
        timer.schedule(task2, new Date(scheduleTime2));
        Thread.sleep(Integer.MAX_VALUE);
    }
}
