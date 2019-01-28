package com.kepler.thread;

public class Demo_NotifyOneThreadService{

    private Object lock;

    public Demo_NotifyOneThreadService(Object lock){
        this.lock = lock;
    }

    public void waitMethod(){
        synchronized (lock){
            System.out.println(Thread.currentThread().getName() + " start");
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "   end");
        }
    }

    public void notifyMethod(){
        synchronized (lock){
            System.out.println(Thread.currentThread().getName() + " start");
            lock.notify();
            System.out.println(Thread.currentThread().getName() + "   end");
        }
    }
}
