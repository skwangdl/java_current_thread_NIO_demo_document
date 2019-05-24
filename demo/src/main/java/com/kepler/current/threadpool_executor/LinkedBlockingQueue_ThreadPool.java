package com.kepler.current.threadpool_executor;

import java.sql.Time;
import java.util.concurrent.*;

public class LinkedBlockingQueue_ThreadPool {
    public static void main(String[] args){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(7,8,
                5, TimeUnit.SECONDS, new LinkedBlockingQueue());
        System.out.println(executor.getCorePoolSize());
        System.out.println(executor.getMaximumPoolSize());
        executor = new ThreadPoolExecutor(7,8,5,
                TimeUnit.SECONDS, new SynchronousQueue<>());
        System.out.println(executor.getCorePoolSize());
        System.out.println(executor.getMaximumPoolSize());
    }
}
