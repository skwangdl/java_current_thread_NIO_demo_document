package com.kepler.thread;

public class Demo_Threadyield extends Thread {

    @Override
    public void run() {
        super.run();
        long startTime = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; i < 10000000; i ++){
            count = count + 1;
            Thread.yield();
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}
