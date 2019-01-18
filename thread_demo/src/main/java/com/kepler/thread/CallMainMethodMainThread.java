package com.kepler.thread;

public class CallMainMethodMainThread{
    private String message;

    public static void main(String[] args){
        System.out.println(Thread.currentThread().getName());
    };

}
