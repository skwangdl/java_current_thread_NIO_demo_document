package com.kepler.thread.threadlocal;

public class DemoRun {
    public static ThreadLocal t1 = new ThreadLocal();

    public static void main(String[] args){
        if(t1.get() == null){
            System.out.println("never get value...");
            t1.set("anystring");
        }
        System.out.println(t1.get());
//        get方法返回当前线程存储的值，不删除
        System.out.println(t1.get());
    }
}
