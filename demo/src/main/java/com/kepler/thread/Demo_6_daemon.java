package com.kepler.thread;

public class Demo_6_daemon extends Thread {
    private int i = 0;

    @Override
    public void run() {
        super.run();
        while (true){
            i ++;
            System.out.println("i=" + (i));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Demo_6_daemon thread = new Demo_6_daemon();
        thread.setDaemon(true);
        thread.start();
        Thread.sleep(5000);
        System.out.println("main thread stop, so daemon threa stop too.");
    }
}
