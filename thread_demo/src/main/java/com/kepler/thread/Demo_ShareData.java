package com.kepler.thread;

public class Demo_ShareData extends Thread {

    private int count = 5;

    @Override
    synchronized public void run() {
        super.run();
        count --;
        System.out.println(Thread.currentThread().getName() + " count: " + count);
    }
}
