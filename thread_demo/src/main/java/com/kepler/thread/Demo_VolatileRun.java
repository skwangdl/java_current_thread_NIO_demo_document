package com.kepler.thread;

import org.junit.Test;

public class Demo_VolatileRun {

    @Test
    public void run(){
        Demo_Volatile dv = new Demo_Volatile();
        dv.start();
        dv.setRunning(false);
    }
}
