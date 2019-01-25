package com.kepler.thread;

import org.junit.Test;

public class Demo_TwoObjectAndTwoLockRun {

    @Test
    public void run(){
        Demo_TwoObjectAndTwoLock d1 = new Demo_TwoObjectAndTwoLock();
        Demo_TwoObjectAndTwoLock d2 = new Demo_TwoObjectAndTwoLock();
        Demo_TwoObjectAndTwoLockThreadA ta = new Demo_TwoObjectAndTwoLockThreadA(d1);
        Demo_TwoObjectAndTwoLockThreadB tb = new Demo_TwoObjectAndTwoLockThreadB(d2);
        ta.start();
        tb.start();
    }


}
