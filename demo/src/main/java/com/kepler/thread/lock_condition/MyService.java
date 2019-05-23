package com.kepler.thread.lock_condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyService {
//    公平锁：先锁定的线程，先得到锁运行，同步队列实现,默认
//    非公平锁：先锁定的线程，不一定先得到锁，CAS机制抢锁，谁抢到谁执行
    private Lock lock = new ReentrantLock();
    public Condition condition_A = lock.newCondition();
    public Condition condition_B = lock.newCondition();

    public void awaitA() throws InterruptedException {
        lock.lock();
        System.out.println("begin awaitA time: " + System.currentTimeMillis() +
                " ThreadName=" + Thread.currentThread().getName());
        condition_A.await();
        System.out.println(" end awaitA time:" + System.currentTimeMillis() +
                " ThreadName=" + Thread.currentThread().getName());
        lock.unlock();
    }

    public void awaitB() throws InterruptedException {
        lock.lock();
        System.out.println("begin awaitB time: " + System.currentTimeMillis() +
                " ThreadName=" + Thread.currentThread().getName());
        condition_B.await();
        System.out.println(" end awaitB time:" + System.currentTimeMillis() +
                " ThreadName=" + Thread.currentThread().getName());
        lock.unlock();
    }

    public void signalAll_A(){
        lock.lock();
        System.out.println(" signalAll_A time: " + System.currentTimeMillis() +
                " ThreadName=" + Thread.currentThread().getName());
        condition_A.signalAll();
        lock.unlock();
    }

    public void signalAll_B(){
        lock.lock();
        System.out.println(" signalAll_B time: " + System.currentTimeMillis() +
                " ThreadName=" + Thread.currentThread().getName());
        condition_B.signalAll();
        lock.unlock();
    }
}
