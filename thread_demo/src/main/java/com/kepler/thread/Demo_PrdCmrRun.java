package com.kepler.thread;

import org.junit.Test;

public class Demo_PrdCmrRun {


    @Test
    public void run(){
        Demo_PrdCmrStack stack = new Demo_PrdCmrStack();
        Demo_PrdCmrStackThreadPop pop = new Demo_PrdCmrStackThreadPop(stack);
        Demo_PrdCmrStackThreadPush push = new Demo_PrdCmrStackThreadPush(stack);
        pop.start();
        push.start();
    }
}
