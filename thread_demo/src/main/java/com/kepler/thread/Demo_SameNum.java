package com.kepler.thread;

public class Demo_SameNum extends Thread {
    private int i;

    @Override
    public void run() {
        super.run();
        System.out.println("i=" + (i--) + "threadName= " + Thread.currentThread().getName());
    }
}
