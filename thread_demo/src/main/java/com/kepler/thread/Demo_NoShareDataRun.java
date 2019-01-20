package com.kepler.thread;

import org.junit.Test;

public class Demo_NoShareDataRun {

    @Test
    public void run(){
        Demo_NoShareData t1 = new Demo_NoShareData("t1");
        Demo_NoShareData t2 = new Demo_NoShareData("t2");
        Demo_NoShareData t3 = new Demo_NoShareData("t3");
        t1.start();
        t2.start();
        t3.start();
    }
}
