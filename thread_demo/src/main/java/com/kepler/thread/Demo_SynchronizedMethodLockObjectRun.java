package com.kepler.thread;

import org.junit.Test;

public class Demo_SynchronizedMethodLockObjectRun {

    @Test
    public void run(){
        Demo_SynchronizedMethodLockObject object = new Demo_SynchronizedMethodLockObject();
        Demo_SynchronizedMethodLockObjectThreadA ta = new Demo_SynchronizedMethodLockObjectThreadA(object);
        Demo_SynchronizedMethodLockObjectThreadB tb = new Demo_SynchronizedMethodLockObjectThreadB(object);
        ta.start();
        tb.start();
        System.out.println("main end");
    }

}
