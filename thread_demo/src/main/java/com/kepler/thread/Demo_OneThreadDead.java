package com.kepler.thread;

public class Demo_OneThreadDead {
    private boolean isContinuePrint = true;

    public boolean isContinuePrint(){
        return isContinuePrint;
    }

    public void setContinuePrint(boolean isContinuePrint){
        this.isContinuePrint = isContinuePrint;
    }

    public void printStringMethod(){
        try {
            while (isContinuePrint == true){
                System.out.println("run printString method threadName= " + Thread.currentThread().getName());
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
