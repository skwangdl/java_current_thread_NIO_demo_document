package com.kepler.thread.executor_service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Run {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List list = new ArrayList();
        list.add(new MyCallableA());
        list.add(new MyCallableB1());
        ExecutorService executor = Executors.newCachedThreadPool();
        // invokeAny 只取得最先完成任务的结果值
        String getValueA = (String) executor.invokeAny(list);
        System.out.println("main get return value: " + getValueA);
        System.out.println("main end !!!");
    }
}
