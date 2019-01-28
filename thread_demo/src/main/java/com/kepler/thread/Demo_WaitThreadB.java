package com.kepler.thread;

public class Demo_WaitThreadB extends Thread {
    private Object lock;

    public Demo_WaitThreadB(Object lock){
        this.lock = lock;
    }

    @Override
    public void run() {
        super.run();
        synchronized (lock){
            System.out.println(Thread.currentThread().getName() + " start");
            lock.notify();
            System.out.println(Thread.currentThread().getName() + "   end");
        }
    }
}
