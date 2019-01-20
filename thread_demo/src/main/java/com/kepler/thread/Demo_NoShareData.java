package com.kepler.thread;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class Demo_NoShareData extends Thread {

    private int count = 5;
    private String name;

    public Demo_NoShareData(String name){
        this.name = name;
    }

    @Override
    public void run() {
        super.run();
        while(count > 0){
            count --;
            System.out.println(Thread.currentThread().getName() + " count: " + count);
        }
    }
}
