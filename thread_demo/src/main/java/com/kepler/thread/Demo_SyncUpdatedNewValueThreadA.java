package com.kepler.thread;

public class Demo_SyncUpdatedNewValueThreadA extends Thread {
    private Demo_SyncUpdatedNewValue value;

    public Demo_SyncUpdatedNewValueThreadA(Demo_SyncUpdatedNewValue value){
        this.value = value;
    }

    @Override
    public void run() {
        super.run();
        value.runMethod();
    }
}
