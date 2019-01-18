package com.kepler.thread;

import org.junit.Test;

public class Demo_MyThreadRun {

    @Test
    public void run(){
        Demo_MyThread t = new Demo_MyThread();
        t.start();
        System.out.println("main start");
    }
}
