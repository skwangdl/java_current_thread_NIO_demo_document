package com.kepler.thread;

import org.junit.Test;

public class Demo_2_safe {
    private int count = 0;

    class MyThread implements Runnable{
        @Override
        public void run() {
            synchronized (Demo_2_safe.class){
                for(int i = 0; i < 1000; i ++){
                    count += 1;
                }
                System.out.println(Thread.currentThread().getName() + " change count:" + count);
            }
        }
    }

    @Test
    public void test(){
        for(int i = 0; i < 10; i ++){
            new Thread(new MyThread()).start();
        }
    }
}
