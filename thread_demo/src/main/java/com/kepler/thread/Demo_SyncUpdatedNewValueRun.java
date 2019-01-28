package com.kepler.thread;

import org.junit.Test;

public class Demo_SyncUpdatedNewValueRun {

    @Test
    public void run(){
        Demo_SyncUpdatedNewValue value = new Demo_SyncUpdatedNewValue();
        Demo_SyncUpdatedNewValueThreadA ta = new Demo_SyncUpdatedNewValueThreadA(value);
        Demo_SyncUpdatedNewValueThreadB tb = new Demo_SyncUpdatedNewValueThreadB(value);
        ta.start();
        tb.start();
    }
}
