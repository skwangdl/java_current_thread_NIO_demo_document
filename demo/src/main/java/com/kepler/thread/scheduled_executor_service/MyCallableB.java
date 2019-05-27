package com.kepler.thread.scheduled_executor_service;

import java.util.concurrent.Callable;

public class MyCallableB implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("callB begin: " + Thread.currentThread().getName() +
                " " + System.currentTimeMillis());
        Thread.sleep(3000);
        System.out.println("callB end: " + Thread.currentThread().getName() +
                " " + System.currentTimeMillis());
        return "return B";
    }
}
