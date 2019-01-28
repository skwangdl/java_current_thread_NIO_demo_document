package com.kepler.thread;

import org.junit.Test;

public class Demo_AtomicRun {

    @Test
    public void run(){
        Demo_AtomicThread thread = new Demo_AtomicThread();
        Thread t1 = new Thread(thread);
        Thread t2 = new Thread(thread);
        Thread t3 = new Thread(thread);
        Thread t4 = new Thread(thread);
        Thread t5 = new Thread(thread);
        Thread t6 = new Thread(thread);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
    }
}
