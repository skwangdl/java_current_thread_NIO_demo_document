package com.kepler.thread;

public class Demo_SyncInstanceThreadA extends Thread {

    private Demo_SyncInstance instance;

    public Demo_SyncInstanceThreadA(Demo_SyncInstance instance){
        this.instance = instance;
    }

    @Override
    public void run() {
        super.run();
        instance.method_2();
    }
}
