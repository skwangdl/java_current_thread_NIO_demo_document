package com.kepler.thread;

public class Demo_MyThread extends Thread {

    @Override
    public void run() {
        super.run();
        System.out.println(Thread.currentThread().getName() + " run !!!" );
    }
}
