package com.kepler.thread;

public class Demo_SyncInstanceThreadB extends Thread {

    private Demo_SyncInstance instance;

    public Demo_SyncInstanceThreadB(Demo_SyncInstance instance){
        this.instance = instance;
    }

    @Override
    public void run() {
        super.run();
        instance.method_2();
    }
}
