package com.kepler.thread.single_instance;

public class MyThread extends Thread {

    @Override
    public void run() {
        try {
            System.out.println(Delay_demo.getInstance().hashCode());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
