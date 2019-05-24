package com.kepler.thread.threadgroup;

public class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println("Thread name: " + Thread.currentThread().getName() + " start");
        while (true){
            if(Thread.currentThread().isInterrupted()){
                System.out.println("Thread name: " + Thread.currentThread().getName() + " end");
                break;
            }
        }

    }
}
