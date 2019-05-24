package com.kepler.current.semaphore;

import java.util.concurrent.Semaphore;

public class Service {
    //确定初始的允许线程数信号量5, 默认为非公平信号，获得信号的顺序与线程启动的顺序无关
    private Semaphore semaphore = new Semaphore(5);

    public void testMethod() throws InterruptedException {
        //获取1个线程信号
        semaphore.acquire(1);
        System.out.println(Thread.currentThread().getName() + " begin timer=" + System.currentTimeMillis());
        Thread.sleep(3000);
        System.out.println(Thread.currentThread().getName() + " end timer=" + System.currentTimeMillis());
        //归还1个线程信号
        System.out.println(Thread.currentThread().getName() + " queue length=" + semaphore.getQueueLength());
        semaphore.release(1);
    }
}
