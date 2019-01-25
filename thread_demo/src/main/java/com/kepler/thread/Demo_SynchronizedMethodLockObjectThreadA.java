package com.kepler.thread;

public class Demo_SynchronizedMethodLockObjectThreadA extends Thread {
    private Demo_SynchronizedMethodLockObject object;

    public Demo_SynchronizedMethodLockObjectThreadA(Demo_SynchronizedMethodLockObject object){
        super();
        this.object = object;
    }

    @Override
    public void run() {
        super.run();
        object.methodA();
    }
}
