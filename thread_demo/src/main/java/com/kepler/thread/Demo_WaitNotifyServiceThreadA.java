package com.kepler.thread;

public class Demo_WaitNotifyServiceThreadA extends Thread {

    private Demo_WaitNotifyService service;

    public Demo_WaitNotifyServiceThreadA(Demo_WaitNotifyService service){
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.waitMethod();
    }
}
