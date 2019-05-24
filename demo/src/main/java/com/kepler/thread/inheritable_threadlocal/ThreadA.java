package com.kepler.thread.inheritable_threadlocal;

public class ThreadA extends Thread {

    @Override
    public void run() {
        for(int i = 0; i < 10; i ++){
            System.out.println("get value from threadA = " + Tools.t1.get());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
