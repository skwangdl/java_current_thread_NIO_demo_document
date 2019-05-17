package com.kepler.thread;


import org.junit.Test;

public class Demo_1_basic {

    class MyThread extends Thread{

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
        }
    }

    class MyThread1 implements Runnable{

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
        }
    }

    @Test
    public void test(){
        new MyThread().start();
        new MyThread().start();
        new Thread(new MyThread1()).start();
        new Thread(new MyThread1()).start();
    }
}
