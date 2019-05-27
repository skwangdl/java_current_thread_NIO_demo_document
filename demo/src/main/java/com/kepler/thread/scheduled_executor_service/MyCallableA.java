package com.kepler.thread.scheduled_executor_service;

import java.util.concurrent.Callable;

public class MyCallableA implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("callA begin: " + Thread.currentThread().getName() +
                " " + System.currentTimeMillis());
        Thread.sleep(3000);
        System.out.println("callA end: " + Thread.currentThread().getName() +
                " " + System.currentTimeMillis());
        return "return A";
    }
}
