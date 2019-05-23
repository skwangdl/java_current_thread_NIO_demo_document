package com.kepler.thread.readwritelock;

public class ThreadRead extends Thread {
    private Service service;

    public ThreadRead(Service service) {
        this.service = service;
    }

    @Override
    public void run() {
        try {
            service.read();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
