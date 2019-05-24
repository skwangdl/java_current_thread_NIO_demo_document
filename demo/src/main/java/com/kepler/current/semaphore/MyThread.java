package com.kepler.current.semaphore;

public class MyThread extends Thread {
    private Service service;

    public MyThread(Service service){
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        try {
            service.testMethod();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
