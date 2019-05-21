package com.kepler.thread;

import org.junit.Test;

public class Demo_3_interrupt extends Thread {

    @Override
    public void run() {
        super.run();
        for(int i = 0; i < 50000; i ++){
            System.out.println("i=" + (i));
        }
    }

    @Test
    public void test() throws InterruptedException {
        Demo_3_interrupt thread1 = new Demo_3_interrupt();
        thread1.start();
        Thread.sleep(100);
        thread1.interrupt();
        //interrupt()方法对线程进行打断，并非立即打断，时间不确定. 抛出异常也可以终止线程
        System.out.println("zzzzzzz");
        System.out.println("thread1 interrupted: " + thread1.isInterrupted());
    }
}
