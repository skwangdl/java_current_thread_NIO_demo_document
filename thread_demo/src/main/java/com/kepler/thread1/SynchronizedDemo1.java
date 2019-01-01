package com.kepler.thread1;

public class SynchronizedDemo1 {

    public static void main(String[] args){
        SynchronizedDemo1 demo = new SynchronizedDemo1();
        demo.run();
    }

    public void run(){
        SynThread t1 = new SynThread();
        SynThread t2 = new SynThread();
        t1.start();
        t2.start();
    }

    public void synMethod(String name) throws Exception {
        synchronized (name){
            System.out.println("in synchronized block :" + name + " " + Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("out synchronized block :" + name + " " + Thread.currentThread().getName());
        }
    }

    class SynThread extends Thread{
        @Override
        public void run() {
            try {
                synMethod("kepler");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
