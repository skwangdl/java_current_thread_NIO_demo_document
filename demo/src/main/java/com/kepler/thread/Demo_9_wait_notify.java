package com.kepler.thread;

import org.junit.Test;

public class Demo_9_wait_notify {

    private Object lock = new Object();

    @Test
    public void methodTest(){
        Demo_9_wait_notify demo = new Demo_9_wait_notify();
        ThreadWait threadWait = new ThreadWait(demo);
        ThreadNotifyAll threadNotifyAll = new ThreadNotifyAll(demo);
        threadWait.start();
        threadNotifyAll.start();
    }

    public void methodWait() throws InterruptedException {
        synchronized (lock){
            System.out.println("start lock");
            Thread.sleep(2000);
            lock.wait();
            System.out.println("end lock");
        }
    }

    public void methodNotifyAll(){
        synchronized (lock){
            System.out.println("start notify all");
            lock.notifyAll();
            System.out.println("end notify all");
        }
    }

    class ThreadWait extends Thread{
        private Demo_9_wait_notify demo;
        public ThreadWait(Demo_9_wait_notify demo){
            this.demo = demo;
        }

        @Override
        public void run() {
            try {
                demo.methodWait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class ThreadNotifyAll extends Thread{
        private Demo_9_wait_notify demo;
        public ThreadNotifyAll(Demo_9_wait_notify demo){
            this.demo = demo;
        }

        @Override
        public void run() {
            demo.methodNotifyAll();
        }
    }

}
