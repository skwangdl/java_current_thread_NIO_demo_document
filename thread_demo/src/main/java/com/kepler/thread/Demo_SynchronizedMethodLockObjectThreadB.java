package com.kepler.thread;

public class Demo_SynchronizedMethodLockObjectThreadB extends Thread {

    private Demo_SynchronizedMethodLockObject object;

    public Demo_SynchronizedMethodLockObjectThreadB(Demo_SynchronizedMethodLockObject object){
        super();
        this.object = object;
    }

    @Override
    public void run() {
        super.run();
        object.methodA();
    }
}
