package com.kepler.thread;

import org.junit.Test;

public class Demo_ThrowExceptionNoLockRun {

    @Test
    public void run() throws InterruptedException {
        Demo_ThrowExceptionNoLock lock = new Demo_ThrowExceptionNoLock();
        Demo_ThrowExceptionNoLockThread ta = new Demo_ThrowExceptionNoLockThread(lock);
        Demo_ThrowExceptionNoLockThread tb = new Demo_ThrowExceptionNoLockThread(lock);
        ta.setName("a");
        tb.setName("b");
        Thread.sleep(3000);
        ta.start();
        tb.start();
        Thread.sleep(3000);
        System.out.println("main end");
    }
}
