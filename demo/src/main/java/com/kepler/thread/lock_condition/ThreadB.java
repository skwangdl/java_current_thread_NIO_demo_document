package com.kepler.thread.lock_condition;

public class ThreadB extends Thread {
    private MyService service;

    public ThreadB(MyService service){
        this.service = service;
    }

    @Override
    public void run() {
        try {
            service.awaitB();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
