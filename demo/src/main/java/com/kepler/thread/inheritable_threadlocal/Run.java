package com.kepler.thread.inheritable_threadlocal;

public class Run {
    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < 10; i ++){
            if(Tools.t1.get() == null){
                Tools.t1.set("this value is inserted by main thread");
            }
            System.out.println("        get value from main thread=" + Tools.t1.get());
            Thread.sleep(100);
        }
        Thread.sleep(5000);
        ThreadA a = new ThreadA();
        a.start();
    }
}
