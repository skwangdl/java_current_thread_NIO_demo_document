package com.kepler.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo_AtomicThread extends Thread {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public void run() {
        super.run();
        for(int i = 0; i < 1000; i ++){
            atomicInteger.incrementAndGet();
            System.out.println(Thread.currentThread().getName() + " " + atomicInteger.toString());
        }
    }
}
