package com.kepler.thread.lock_condition;

public class ThreadA extends Thread {
    private MyService service;

    public ThreadA(MyService service){
        this.service = service;
    }

    @Override
    public void run() {
        try {
            service.awaitA();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
