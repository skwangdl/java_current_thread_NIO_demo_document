package com.kepler.thread;

public class Demo_SyncUpdatedNewValue {

    private boolean isContinueRun = true;

    public void runMethod(){
        String lock = new String();
        while (isContinueRun == true){
            synchronized (lock){

            }

        }
        System.out.println("stop it");
    }

    public void stopMethod(){
        isContinueRun = false;
    }

}
