package com.kepler.thread;

public class Demo_RandomThread extends Thread {

    @Override
    public void run() {
        super.run();
        for(int i = 0; i < 1000; i ++){
            System.out.print(Thread.currentThread().getName());
        }
    }
}
