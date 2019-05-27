package com.kepler.thread.scheduled_executor_service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class RunSchedule {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Callable> callableList = new ArrayList<Callable>();
        callableList.add(new MyCallableA());
        callableList.add(new MyCallableB());
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        System.out.println("schedule not blocking begin : " + System.currentTimeMillis());
        ScheduledFuture<String> futureA = executor.schedule(callableList.get(0), 4L, TimeUnit.SECONDS);
        ScheduledFuture<String> futureB = executor.schedule(callableList.get(1), 4L, TimeUnit.SECONDS);
        System.out.println("return value A : " + futureA.get() + " " + System.currentTimeMillis());
        System.out.println("return value B : " + futureB.get() + " " + System.currentTimeMillis());
    }
}
