package com.kepler.thread;

import org.junit.Test;

public class Demo_5_priority extends Thread {

    @Override
    public void run() {
        for(int i = 0; i < 10000; i ++){
            System.out.println(Thread.currentThread().getName() + " i:" + i);
        }
    }

    public static void main(String[] args){
        Demo_5_priority thread1 = new Demo_5_priority();
        Demo_5_priority thread2 = new Demo_5_priority();
        thread1.setPriority(10);
        thread2.setPriority(1);
        thread1.start();
        thread2.start();
    }
}
