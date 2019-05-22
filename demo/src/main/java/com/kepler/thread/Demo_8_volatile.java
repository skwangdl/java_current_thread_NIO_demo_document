package com.kepler.thread;

public class Demo_8_volatile extends Thread {
    volatile private boolean isRunning = true;

    public boolean isRunning(){
        return isRunning;
    }

    public void setRunning(boolean isRunning){
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        System.out.println("is running...");
        while (isRunning == true){
        }
        System.out.println("thread stop...");
    }

    public static void main(String[] args) throws InterruptedException {
        Demo_8_volatile thread = new Demo_8_volatile();
        thread.start();
        Thread.sleep(1000);
        thread.setRunning(false);
        System.out.println("have been seted false");
    }
}
