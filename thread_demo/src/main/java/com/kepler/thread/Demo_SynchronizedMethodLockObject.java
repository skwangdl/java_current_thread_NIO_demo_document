package com.kepler.thread;

public class Demo_SynchronizedMethodLockObject {

    synchronized public void methodA(){
        System.out.println("begin methodA threadName=" + Thread.currentThread().getName());
        System.out.println("end");
    }

}
