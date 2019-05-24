package com.kepler.current.callable_future;

import java.util.concurrent.*;

public class MyCallable implements Callable<String> {
    private int age;
    public MyCallable(int age){
        this.age = age;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(2000);
        return "return age: " + age;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyCallable myCallable = new MyCallable(100);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,3,
                5L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        Future<String> future = executor.submit(myCallable);
        System.out.println("main time: " + System.currentTimeMillis());
        System.out.println(future.get());
        System.out.println("main time: " + System.currentTimeMillis());
        executor.shutdownNow();
    }
}
