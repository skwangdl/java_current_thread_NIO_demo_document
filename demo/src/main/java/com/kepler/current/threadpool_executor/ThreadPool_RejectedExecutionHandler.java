package com.kepler.current.threadpool_executor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool_RejectedExecutionHandler {

    public static void main(String[] args){
        MyRunnable myRunnable_1 = new MyRunnable("1");
        MyRunnable myRunnable_2 = new MyRunnable("2");
        MyRunnable myRunnable_3 = new MyRunnable("3");
        MyRunnable myRunnable_4 = new MyRunnable("4");
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2,3,9999L,
                TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        pool.setRejectedExecutionHandler(new MyRejectedExecutionHandler());
        pool.execute(myRunnable_1);
        pool.execute(myRunnable_2);
        pool.execute(myRunnable_3);
        pool.execute(myRunnable_4);
    }

    static class MyRunnable implements Runnable {
        private String name;
        public MyRunnable(String name) {
            this.name = name;
        }
        public String getName(){
            return name;
        }
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() +
                        " Time: " + System.currentTimeMillis());
                Thread.sleep(3000);
                System.out.println(Thread.currentThread().getName() +
                        " Time: " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class MyRejectedExecutionHandler implements RejectedExecutionHandler{
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println(((MyRunnable) r).getName() + " be rejected");
        }
    }
}
