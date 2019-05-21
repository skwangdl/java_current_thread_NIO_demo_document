package com.kepler.thread;

public class Demo_7_synchronized {

    //method_1, method_3 锁为当前对象this;
    //method_2 锁为lock对象
    //method_4 锁为类Demo_7_synchronized的class对象，该对象为单例
    //method_5 锁为类Demo_7_synchronized的class对象，该对象为单例

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
