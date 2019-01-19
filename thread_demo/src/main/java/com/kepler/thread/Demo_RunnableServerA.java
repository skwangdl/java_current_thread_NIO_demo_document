package com.kepler.thread;

public class Demo_RunnableServerA extends Demo_RunnableServerB implements Runnable{

    public void aSaveMethod(){
        System.out.println("A data saved");
    }

    @Override
    public void run() {
        aSaveMethod();
    }
}
