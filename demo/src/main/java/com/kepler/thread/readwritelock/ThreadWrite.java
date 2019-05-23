package com.kepler.thread.readwritelock;

public class ThreadWrite extends Thread {
    private Service service;

    public ThreadWrite(Service service) {
        this.service = service;
    }

    @Override
    public void run() {
        try {
            service.write();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
