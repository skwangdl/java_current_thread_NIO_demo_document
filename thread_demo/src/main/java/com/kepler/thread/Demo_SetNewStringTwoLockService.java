package com.kepler.thread;

public class Demo_SetNewStringTwoLockService {
    private String lock = "123";

    public void testMethod(){
        synchronized (lock){
            System.out.println(Thread.currentThread().getName() + " begin");
            lock = "456";
            System.out.println(Thread.currentThread().getName() + "   end");
        }
    }
}
