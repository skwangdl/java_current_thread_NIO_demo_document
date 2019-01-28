package com.kepler.thread;

import org.junit.Test;

public class Demo_OneThreadDeadRun {

    @Test
    public void run(){
        Demo_OneThreadDead td = new Demo_OneThreadDead();
        td.printStringMethod();
        System.out.println("want to stop it : " + Thread.currentThread().getName());
        td.setContinuePrint(true);
    }
}
