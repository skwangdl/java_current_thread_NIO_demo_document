package com.kepler.thread.threadgroup;

public class Run {
    public static void main(String[] args) throws InterruptedException {
        MyThread t1 = new MyThread();
        MyThread t2 = new MyThread();
        ThreadGroup group = new ThreadGroup("My thread group");
        Thread ta = new Thread(group, t1);
        Thread tb = new Thread(group, t2);
        ta.start();
        tb.start();
        Thread.sleep(3000);
        group.interrupt();
    }
}
