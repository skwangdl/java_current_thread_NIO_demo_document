package com.kepler.thread;

public class Demo_ProductConsumerOneToOneGetThread extends Thread {

    private Demo_ProductConsumerOneToOneGet getThread;

    public Demo_ProductConsumerOneToOneGetThread(Demo_ProductConsumerOneToOneGet getThread){
        this.getThread = getThread;
    }

    @Override
    public void run() {
        super.run();
        getThread.getValue();
    }
}
