package com.kepler.thread;

public class Demo_WaitNotifyServiceThreadB extends Thread {
    private Demo_WaitNotifyService service;

    public Demo_WaitNotifyServiceThreadB(Demo_WaitNotifyService service){
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.notifyMethod();
    }
}
