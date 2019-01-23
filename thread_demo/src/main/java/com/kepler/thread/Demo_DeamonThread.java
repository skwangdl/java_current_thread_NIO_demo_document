package com.kepler.thread;

public class Demo_DeamonThread extends Thread {
    private int i = 0;

    @Override
    public void run() {
        super.run();
        while (true){
            i++;
            System.out.println("i=" + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
