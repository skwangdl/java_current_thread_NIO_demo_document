package com.kepler.current.threadpool_executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolRejectStrategy {
    public static void main(String[] args){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + " run end!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //AbortPolicy:：当任务添加到线程池中被拒绝时，将抛出RejectedExecutionException，默认
        //CallerRunsPolicy：当任务添加到线程池中被拒绝时，会使用调用线程池的Thread线程对象处理被拒绝的任务
        //DiscardOldestPolicy：当任务添加到线程池中被拒绝时，线程池会放弃等待队列中的最旧的未处理任务，然后将被拒绝的任务加入队列
        //DiscardPolicy：当任务添加到线程池中被拒绝时，线程池将丢弃被拒绝的任务
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,3,5,
                TimeUnit.SECONDS, new ArrayBlockingQueue(2), new ThreadPoolExecutor.AbortPolicy());
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable); // 报错
    }
}
