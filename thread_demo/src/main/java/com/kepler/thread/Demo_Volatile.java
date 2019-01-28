package com.kepler.thread;

public class Demo_Volatile extends Thread{

    volatile private boolean isRunning = true;

    public boolean isRunning(){
        return isRunning;
    }

    public void setRunning(boolean isRunning){
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        super.run();
        System.out.println("in running");
        while (isRunning == true){

        }
        System.out.println("thread stopped");
    }
}
