package com.kepler.thread;

        import org.junit.Test;

public class Demo_WaitRun {

    @Test
    public void run(){
        Object lock = new Object();
        Demo_WaitThreadA ta = new Demo_WaitThreadA(lock);
        ta.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Demo_WaitThreadB tb = new Demo_WaitThreadB(lock);
        tb.start();
    }
}
