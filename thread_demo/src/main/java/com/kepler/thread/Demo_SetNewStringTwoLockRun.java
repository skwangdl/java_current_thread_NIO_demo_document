package com.kepler.thread;

import org.junit.Test;

public class Demo_SetNewStringTwoLockRun {

    @Test
    public void run() throws InterruptedException {
        Demo_SetNewStringTwoLockService service = new Demo_SetNewStringTwoLockService();
        Demo_SetNewStringTwoLockThreadA ta = new Demo_SetNewStringTwoLockThreadA(service);
        Demo_SetNewStringTwoLockThreadB tb = new Demo_SetNewStringTwoLockThreadB(service);
        ta.start();
        Thread.sleep(50);
        tb.start();
    }
}
