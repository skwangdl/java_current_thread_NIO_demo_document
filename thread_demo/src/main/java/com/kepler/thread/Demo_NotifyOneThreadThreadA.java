package com.kepler.thread;

public class Demo_NotifyOneThreadThreadA extends Thread {

    private Demo_NotifyOneThreadService service;

    public Demo_NotifyOneThreadThreadA(Demo_NotifyOneThreadService service){
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.notifyMethod();
    }
}
