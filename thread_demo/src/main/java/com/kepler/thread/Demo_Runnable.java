package com.kepler.thread;

public class Demo_Runnable implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " running!!");
    }
}
