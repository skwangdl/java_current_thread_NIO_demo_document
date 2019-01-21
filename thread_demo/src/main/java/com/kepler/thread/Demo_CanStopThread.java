package com.kepler.thread;

public class Demo_CanStopThread extends Thread {

    @Override
    public void run() {
        super.run();
        try{
            for(int i = 0; i < 100000; i ++){
                if(this.isInterrupted()){
                    System.out.println("can be stopped");
                    throw new InterruptedException();
                }
            }
            System.out.println("if this message show, thread can't stop");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
