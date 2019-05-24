package com.kepler.current.completion_service;

import java.util.concurrent.*;

public class MyCallable implements Callable<String> {
    private int sleepValue;
    private String username;

    public MyCallable(int sleepValue, String username){
        this.sleepValue = sleepValue;
        this.username = username;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(sleepValue);
        return "this is return value: " + username ;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        MyCallable call_1 = new MyCallable(5000, "A");
        MyCallable call_2 = new MyCallable(4000, "B");
        MyCallable call_3 = new MyCallable(3000, "C");
        MyCallable call_4 = new MyCallable(2000, "D");
        MyCallable call_5 = new MyCallable(1000, "E");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 12, 10,
                TimeUnit.SECONDS, new LinkedBlockingQueue(1));
        CompletionService service = new ExecutorCompletionService(executor);
        service.submit(call_1);
        service.submit(call_2);
        service.submit(call_3);
        service.submit(call_4);
        service.submit(call_5);
        //service.take()获取最先执行完毕的callable结果，take()方法有阻塞性
        Future future1 = service.take();
        System.out.println(future1.get());
        Future future2 = service.take();
        System.out.println(future2.get());
        Future future3 = service.take();
        System.out.println(future3.get());
        Future future4 = service.take();
        System.out.println(future4.get());
        Future future5 = service.take();
        System.out.println(future5.get());
    }
}
