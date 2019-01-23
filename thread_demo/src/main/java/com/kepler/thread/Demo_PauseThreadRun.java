package com.kepler.thread;

import org.junit.Test;

public class Demo_PauseThreadRun {

    @Test
    public void run() throws InterruptedException {
        Demo_PauseThread t = new Demo_PauseThread();
        t.start();
        Thread.sleep(2000);
        t.suspend();
        System.out.println(t.getI());
        t.resume();
        Thread.sleep(2000);
        System.out.println(t.getI());
        t.suspend();
        System.out.println(t.getI());

    }
}
