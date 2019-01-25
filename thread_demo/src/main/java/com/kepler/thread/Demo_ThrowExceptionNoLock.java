package com.kepler.thread;

public class Demo_ThrowExceptionNoLock {

    synchronized public void testMethod() throws Exception {
        System.out.println(Thread.currentThread().getName() + " start");

        String name = Thread.currentThread().getName();
        if(Thread.currentThread().getName().equals("a")){
            System.out.println(Thread.currentThread().getName() + " throw Exception");
            throw new Exception("I throw a Exception: " + name);
        }else{
            Thread.sleep(2000);
        }
        System.out.println(Thread.currentThread().getName() + " end");
    }
}
