package com.kepler.thread;

public class Demo_SetNewStringTwoLockThreadA extends Thread{
    private Demo_SetNewStringTwoLockService service;

    public Demo_SetNewStringTwoLockThreadA(Demo_SetNewStringTwoLockService service){
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.testMethod();
    }
}
