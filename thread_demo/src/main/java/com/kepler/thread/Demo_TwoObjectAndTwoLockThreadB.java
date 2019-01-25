package com.kepler.thread;

public class Demo_TwoObjectAndTwoLockThreadB extends Thread{
    private Demo_TwoObjectAndTwoLock demo;

    public Demo_TwoObjectAndTwoLockThreadB(Demo_TwoObjectAndTwoLock demo){
        super();
        this.demo = demo;
    }

    @Override
    public void run() {
        super.run();
        demo.addNum("b");
    }
}
