package com.kepler.thread;

import org.junit.Test;

public class Demo_WaitNotifyServiceRun {

    @Test
    public void run(){
        Demo_WaitNotifyService service = new Demo_WaitNotifyService();
        Demo_WaitNotifyServiceThreadA ta = new Demo_WaitNotifyServiceThreadA(service);
        Demo_WaitNotifyServiceThreadB tb = new Demo_WaitNotifyServiceThreadB(service);
        ta.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tb.start();
    }
}
