package com.kepler.thread;

import org.junit.Test;

public class Demo_RandomThreadRun {

    @Test
    public void run(){
        Demo_RandomThread t = new Demo_RandomThread();
        t.start();

        for(int i = 0; i < 100; i ++){
            System.out.println(Thread.currentThread().getName());
        }
    }
}
