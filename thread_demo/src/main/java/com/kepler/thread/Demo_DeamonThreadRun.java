package com.kepler.thread;

import org.junit.Test;

public class Demo_DeamonThreadRun {

    @Test
    public void run() throws InterruptedException {
        Demo_DeamonThread d = new Demo_DeamonThread();
        d.setDaemon(true);
        d.start();
        Thread.sleep(4000);
        System.out.println("main end");
    }
}
