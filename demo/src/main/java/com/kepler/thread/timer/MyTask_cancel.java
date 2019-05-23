package com.kepler.thread.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyTask_cancel extends TimerTask {

    private String name;

    public MyTask_cancel(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("task run time: " + System.currentTimeMillis() + " name： " + name);
        if(name.equals("A")){
            this.cancel();
            System.out.println("task cancel itself, name: " + name);
        }
    }

    public static void main(String[] args){
        long nowTime = System.currentTimeMillis();
        System.out.println("now time: " + nowTime);
        System.out.println("schedule time: " + nowTime);
        MyTask_cancel task1 = new MyTask_cancel("A");
        MyTask_cancel task2 = new MyTask_cancel("B");
        Timer timer = new Timer();
        timer.schedule(task1, new Date(nowTime), 2000);
        timer.schedule(task2, new Date(nowTime), 2000);
    }
}
