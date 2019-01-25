package com.kepler.thread;

import org.junit.Test;

public class Demo_ReentryLockRun {

    @Test
    public void run(){
        Demo_ReentryLock l = new Demo_ReentryLock();
        Demo_ReentryLockThread t = new Demo_ReentryLockThread(l);
        t.start();
    }
}
