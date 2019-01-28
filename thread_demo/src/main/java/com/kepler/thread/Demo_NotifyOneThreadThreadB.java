package com.kepler.thread;

public class Demo_NotifyOneThreadThreadB extends Thread {

    private Demo_NotifyOneThreadService service;

    public Demo_NotifyOneThreadThreadB(Demo_NotifyOneThreadService service){
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.waitMethod();
    }
}
