package com.kepler.current.threadpool_executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewCachedThreadPoolReUsed {
    public static void main(String[] args){
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i = 0; i < 10; i ++){
            executorService.execute(new MyRunnable(("" + i)));
        }
    }

    static class MyRunnable implements Runnable{
        private String name;
        public MyRunnable(String name){
            this.name = name;
        }
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " username="
                    + name + " begin: " + System.currentTimeMillis());
            System.out.println(Thread.currentThread().getName() + " username="
                    + name + "   end: " + System.currentTimeMillis());
        }
    }
}
