package com.kepler.thread;

import org.junit.Test;

public class Demo_CanStopThreadRun {

    @Test
    public void run(){
        Demo_CanStopThread d = new Demo_CanStopThread();
        d.start();
        d.interrupt();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main end");
    }
}
