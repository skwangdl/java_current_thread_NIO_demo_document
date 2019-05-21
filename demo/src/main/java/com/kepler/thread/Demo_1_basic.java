package com.kepler.thread;


import org.junit.Test;

public class Demo_1_basic {

    class MyThread extends Thread{
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " id: " + this.getId());
        }
    }

    class MyThread1 implements Runnable{
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
            System.out.println("isAlive: " + Thread.currentThread().isAlive());
            try {
                System.out.println("sleep 2 second begin");
                Thread.sleep(2000);
                System.out.println("sleep 2 second end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
