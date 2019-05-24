package com.kepler.current.exchanger;

import java.util.concurrent.Exchanger;

public class MyThread extends Thread {
    private Exchanger<String> exchanger;

    public MyThread(Exchanger<String> exchanger){
        super();
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " get value from other thread: " + exchanger.exchange("BBB"));
            System.out.println(Thread.currentThread().getName() + " end...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
