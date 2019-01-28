package com.kepler.thread;

public class Demo_SyncUpdatedNewValueThreadB extends Thread {
    private Demo_SyncUpdatedNewValue value;

    public Demo_SyncUpdatedNewValueThreadB(Demo_SyncUpdatedNewValue value){
        this.value = value;
    }

    @Override
    public void run() {
        super.run();
        value.stopMethod();
    }
}
