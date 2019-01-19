package com.kepler.thread;

public class Demo_ThreadSort extends Thread {
    private int i;

    public Demo_ThreadSort(int i){
        this.i = i;
    }

    @Override
    public void run() {
        super.run();
        System.out.println(Thread.currentThread().getName() + " "  + i);
    }
}
