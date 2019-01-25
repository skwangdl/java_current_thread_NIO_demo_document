package com.kepler.thread;

public class Demo_TwoObjectAndTwoLockThreadA extends Thread {
    private Demo_TwoObjectAndTwoLock demo;

    public Demo_TwoObjectAndTwoLockThreadA(Demo_TwoObjectAndTwoLock demo){
        super();
        this.demo = demo;
    }

    @Override
    public void run() {
        super.run();
        demo.addNum("a");
    }
}
