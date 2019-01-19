package com.kepler.thread;

import org.junit.Test;

public class Demo_RunnableRun {

    @Test
    public void run(){
        Demo_Runnable r = new Demo_Runnable();
        Thread thread =  new Thread(r);
        thread.start();
        System.out.println("end");
    }
}
