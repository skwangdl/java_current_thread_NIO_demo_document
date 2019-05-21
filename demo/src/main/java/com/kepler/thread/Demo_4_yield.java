package com.kepler.thread;

import org.junit.Test;

public class Demo_4_yield extends Thread {

    @Override
    public void run() {
        long beginTime = System.currentTimeMillis();
        int count = 0;
        for(int i = 0; i < 5000000; i ++){
            // 释放当前线程所占CPU资源，当前线程挂起直到CPU时间片切换到当前线程，并继续运行
            Thread.yield();
            count = count + (i + 1);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("use time: " + (endTime - beginTime) + " ms");
    }

    @Test
    public void test(){
        Demo_4_yield thread = new Demo_4_yield();
        thread.start();
    }
}
