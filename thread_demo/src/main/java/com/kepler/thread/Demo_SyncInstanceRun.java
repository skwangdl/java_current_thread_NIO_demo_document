package com.kepler.thread;

import org.junit.Test;

public class Demo_SyncInstanceRun {


    @Test
    public void run(){
        Object lock = new Object();
        Demo_SyncInstance instance = new Demo_SyncInstance(lock);
        Demo_SyncInstanceThreadA ta = new Demo_SyncInstanceThreadA(instance);
        Demo_SyncInstanceThreadB tb = new Demo_SyncInstanceThreadB(instance);
        ta.start();
        tb.start();
    }
}
