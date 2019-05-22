package com.kepler.thread.threadlocal;

public class DemoAnalyze extends Thread {
    private ThreadLocal threadLocal;

    public DemoAnalyze(ThreadLocal threadLocal) {
        this.threadLocal = threadLocal;
    }

    @Override
    public void run() {
        threadLocal.set(Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("get value: " + threadLocal.get());
    }

    public static void main(String[] args){
        ThreadLocal threadLocal = new ThreadLocal();
        DemoAnalyze d1 = new DemoAnalyze(threadLocal);
        DemoAnalyze d2 = new DemoAnalyze(threadLocal);
        d1.start();
        d2.start();
    }
}
