package com.kepler.current.threadpool_executor;

import java.sql.Time;
import java.util.concurrent.*;

public class LinkedBlockingQueue_ThreadPool {
    public static void main(String[] args){
        // 核心线程数  最大线程数  线程结束后实例持续时间 线程等待队列
        ThreadPoolExecutor executor = new ThreadPoolExecutor(7,8,
                5, TimeUnit.SECONDS, new LinkedBlockingQueue());
        System.out.println(executor.getCorePoolSize());
        System.out.println(executor.getMaximumPoolSize());
        executor = new ThreadPoolExecutor(7,8,5,
                TimeUnit.SECONDS, new SynchronousQueue<>());
        System.out.println(executor.getCorePoolSize());
        System.out.println(executor.getMaximumPoolSize());
        executor.shutdownNow();
    }
}
