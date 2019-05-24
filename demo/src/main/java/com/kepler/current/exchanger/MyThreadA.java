package com.kepler.current.exchanger;

import java.util.concurrent.Exchanger;

public class MyThreadA extends Thread {
    private Exchanger<String> exchanger;

    public MyThreadA(Exchanger<String> exchanger){
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        super.run();
        try {
            System.out.println(Thread.currentThread().getName() + " get value from other thread: " + exchanger.exchange("AAA"));
            System.out.println(Thread.currentThread().getName() + " end...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
