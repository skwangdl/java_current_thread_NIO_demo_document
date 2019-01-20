package com.kepler.thread;

import org.junit.Test;

public class Demo_ShareDataRun {

    @Test
    public void run(){
        Demo_ShareData shareData = new Demo_ShareData();
        Thread t1 = new Thread(shareData);
        Thread t2 = new Thread(shareData);
        Thread t3 = new Thread(shareData);
        Thread t4 = new Thread(shareData);
        Thread t5 = new Thread(shareData);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }


}
