package com.kepler.thread.join;

public class Run {
    public static void main(String[] args) throws InterruptedException {
        MyThread myThread = new MyThread();
        myThread.start();
        myThread.join();
        System.out.println("main end after mythread end...");
    }
}
