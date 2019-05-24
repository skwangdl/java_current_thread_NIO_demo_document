package com.kepler.current.exchanger;

import java.util.concurrent.Exchanger;

public class Run {
    public static void main(String[] args) throws InterruptedException {
        Exchanger<String> exchanger = new Exchanger<String>();
        MyThread mythreadA = new MyThread(exchanger);
        MyThread mythread = new MyThread(exchanger);
        mythreadA.start();
        Thread.sleep(1000);
        mythread.start();
        System.out.println("main end...");
    }
}
