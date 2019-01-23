package com.kepler.thread;

public class Demo_ThreadLock extends Thread {
    private long i = 0;

    @Override
    public void run() {
        super.run();
        while(true){
            i++;
            System.out.println(i);
        }
    }
}
