package com.kepler.current.callable_future;

import java.util.concurrent.*;

public class MyCallableDemo implements Callable<String> {
    private int age;
    public MyCallableDemo(int age){
        this.age = age;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(2000);
        return "return age: " + age;
    }
    // callable 有返回值，可以抛出异常， runnable 无返回值，不可以抛出异常
    //future.get()具有阻塞性，同步，如果有多个get按顺序执行
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyCallableDemo myCallable = new MyCallableDemo(100);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,3,
                5L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        Future<String> future = executor.submit(myCallable);
        System.out.println("main time: " + System.currentTimeMillis());
        System.out.println(future.get());
        System.out.println("main time: " + System.currentTimeMillis());
        executor.shutdownNow();
    }
}
