package com.kepler.thread.readwritelock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Service {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    //读写锁中，只有读锁与读锁不互斥，其他情况都互斥

    public void write() throws InterruptedException {
        lock.writeLock().lock();
        System.out.println("get write lock " + Thread.currentThread().getName() + " "
                + System.currentTimeMillis());
        Thread.sleep(10000);
        lock.writeLock().unlock();
    }

    public void read() throws InterruptedException {
        lock.readLock().lock();
        System.out.println("get read lock " + Thread.currentThread().getName() + " "
                + System.currentTimeMillis());
        Thread.sleep(10000);
        lock.readLock().unlock();
    }

    public static void main(String[] args){
        Service service = new Service();
        ThreadRead threadRead = new ThreadRead(service);
        ThreadWrite threadWrite = new ThreadWrite(service);
        threadRead.setName("read thread");
        threadWrite.setName("write thread");
        threadRead.start();
        threadWrite.start();
    }
}
