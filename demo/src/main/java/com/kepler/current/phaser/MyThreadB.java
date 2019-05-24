package com.kepler.current.phaser;

public class MyThreadB extends Thread {
    public  MyService myService;

    public MyThreadB(MyService myService){
        this.myService = myService;
    }

    @Override
    public void run() {
        try {
            myService.arrivedLeef();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
