package com.kepler.thread;

import org.junit.Test;

public class Demo_HasSelfPrivateNumRun {

    @Test
    public void run(){
        Demo_HasSelfPrivateNum demo = new Demo_HasSelfPrivateNum();
        Demo_HasSelfPrivateNumThreadA a = new Demo_HasSelfPrivateNumThreadA(demo);
        Demo_HasSelfPrivateNumThreadB b = new Demo_HasSelfPrivateNumThreadB(demo);
        a.start();
        b.start();
    }
}
