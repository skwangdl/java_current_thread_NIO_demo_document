package com.kepler.current.countdownlatch;

public class MyThread extends Thread {
    private MyService service;

    public MyThread(MyService service) {
        this.service = service;
    }

    @Override
    public void run() {
        try {
            service.testMethod();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
