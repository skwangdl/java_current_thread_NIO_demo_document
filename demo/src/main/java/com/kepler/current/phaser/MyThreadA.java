package com.kepler.current.phaser;

import com.kepler.thread.lock.MyThread;

public class MyThreadA extends Thread {
    public  MyService myService;

    public MyThreadA(MyService myService){
        this.myService = myService;
    }

    @Override
    public void run() {
        try {
            myService.arrivedRoot();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
