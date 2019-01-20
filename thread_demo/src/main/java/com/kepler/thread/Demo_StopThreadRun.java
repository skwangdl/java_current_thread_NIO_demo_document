package com.kepler.thread;

import org.junit.Test;

public class Demo_StopThreadRun {
    @Test
    public void run() throws InterruptedException {
        Demo_StopThread t = new Demo_StopThread();
        t.start();
        Thread.sleep(1000);
        t.interrupt();
        System.out.println("is interrupted: " + t.isInterrupted());
        System.out.println("is interrupted: " + t.isInterrupted());
        System.out.println("stop thread ~~~~~");
    }
}
