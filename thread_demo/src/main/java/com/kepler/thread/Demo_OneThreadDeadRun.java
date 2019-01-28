package com.kepler.thread;

import org.junit.Test;

public class Demo_OneThreadDeadRun {

    @Test
    public void run(){
        Demo_OneThreadDead td = new Demo_OneThreadDead();
        new Thread(td).start();
        System.out.println("want to stop it : " + Thread.currentThread().getName());
        td.setContinuePrint(true);
    }
}
