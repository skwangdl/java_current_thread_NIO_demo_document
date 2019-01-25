package com.kepler.thread;

public class Demo_ReentryLock {

    synchronized public void service_1(){
        System.out.println("service1");
        service_2();
    }

    synchronized public void service_2(){
        System.out.println("get lock");
    }
}
