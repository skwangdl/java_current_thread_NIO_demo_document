package com.kepler.thread.scheduled_executor_service;

import java.util.concurrent.Callable;

public class MyRunnable implements Runnable{

    @Override
    public void run() {
        System.out.println("callA begin: " + Thread.currentThread().getName() +
                " " + System.currentTimeMillis());
        System.out.println("callA end: " + Thread.currentThread().getName() +
                " " + System.currentTimeMillis());
    }
}
