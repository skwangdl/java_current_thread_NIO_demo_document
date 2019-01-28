package com.kepler.thread;

public class Demo_SyncInstance {
    private Object lock;

    public Demo_SyncInstance(Object lock){
        this.lock = lock;
    }

    synchronized static public void method_1(){

    }

    public void method_2(){
        synchronized (Demo_SyncInstance.class){

        }
    }

    synchronized public void method_3(){

    }

    public void method_4(){
        synchronized (this){

        }
    }

    public void method_5(){
        synchronized (lock){

        }
    }
}
