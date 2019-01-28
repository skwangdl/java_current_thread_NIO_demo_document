package com.kepler.thread;

public class Demo_SetNewStringTwoLockThreadB extends Thread{
    private Demo_SetNewStringTwoLockService service;

    public Demo_SetNewStringTwoLockThreadB(Demo_SetNewStringTwoLockService service){
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.testMethod();
    }
}
