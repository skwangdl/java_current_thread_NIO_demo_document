package com.kepler.current.countdownlatch;

public class Run {
    public static void main(String[] args) throws InterruptedException {
        MyService service = new MyService();
        MyThread t1 = new MyThread(service);
        MyThread t2 = new MyThread(service);
        t1.start();
        Thread.sleep(5000);
        t2.start();
        System.out.println("main thread end...");
    }
}
