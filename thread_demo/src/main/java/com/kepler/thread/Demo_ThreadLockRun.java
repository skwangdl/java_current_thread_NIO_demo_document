package com.kepler.thread;

import org.junit.Test;

public class Demo_ThreadLockRun {

    @Test
    public void run() throws InterruptedException {
        Demo_ThreadLock d = new Demo_ThreadLock();
        d.start();
        Thread.sleep(2000);
        d.suspend();
        System.out.println("main end");
    }
}
