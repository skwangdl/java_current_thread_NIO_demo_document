package com.kepler.thread;

public class Demo_7_synchronized extends Thread {
    private Object lock = new Object();

    synchronized public void method_1() throws InterruptedException {
        Thread.sleep(1000);
        for(int i = 0; i < 1000; i ++){
            System.out.println(Thread.currentThread().getName());
        }
    }

    public void method_2() throws InterruptedException {
        Thread.sleep(1000);
        synchronized (lock){
            for(int i = 0; i < 1000; i ++){
                System.out.println(Thread.currentThread().getName());
            }
        }
    }

    public void method_3() throws InterruptedException {
        Thread.sleep(1000);
        synchronized (this){
            for(int i = 0; i < 1000; i ++){
                System.out.println(Thread.currentThread().getName());
            }
        }
    }

    public void method_4() throws InterruptedException {
        Thread.sleep(1000);
        synchronized (Demo_7_synchronized.class){
            for(int i = 0; i < 1000; i ++){
                System.out.println(Thread.currentThread().getName());
            }
        }
    }

    synchronized public static void method_5() throws InterruptedException {
        Thread.sleep(1000);
        for(int i = 0; i < 1000; i ++){
            System.out.println(Thread.currentThread().getName());
        }
    }
}
