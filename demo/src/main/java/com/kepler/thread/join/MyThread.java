package com.kepler.thread.join;

public class MyThread extends Thread {

    @Override
    public void run() {
        super.run();
        System.out.println("MyThread start...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("MyThread end...");
    }
}
