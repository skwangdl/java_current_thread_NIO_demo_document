package com.kepler.thread;

import org.junit.Test;

public class Demo_SameNumRun {

    @Test
    public void run(){
        Demo_SameNum t = new Demo_SameNum();
        Thread t1 = new Thread(t);
        Thread t2 = new Thread(t);
        Thread t3 = new Thread(t);
        t1.start();
        t2.start();
        t3.start();
    }
}
