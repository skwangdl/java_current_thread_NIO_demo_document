package com.kepler.current.threadpool_executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewCachedThreadPoolDemo {
    public static void main(String[] args){
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("first begin :" + System.currentTimeMillis());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("first end :" + System.currentTimeMillis());
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("second begin :" + System.currentTimeMillis());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("second end :" + System.currentTimeMillis());
            }
        });
    }
}
