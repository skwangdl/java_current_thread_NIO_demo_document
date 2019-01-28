package com.kepler.thread;

import org.junit.Test;

public class Demo_NotifyOneThreadRun {

    @Test
    public void run(){
        Object lock = new Object();
        Demo_NotifyOneThreadService service = new Demo_NotifyOneThreadService(lock);
        Demo_NotifyOneThreadThreadB tb1 = new Demo_NotifyOneThreadThreadB(service);
        Demo_NotifyOneThreadThreadB tb2 = new Demo_NotifyOneThreadThreadB(service);
        Demo_NotifyOneThreadThreadA ta = new Demo_NotifyOneThreadThreadA(service);
        tb1.start();
        tb2.start();
        ta.start();
    }
}
