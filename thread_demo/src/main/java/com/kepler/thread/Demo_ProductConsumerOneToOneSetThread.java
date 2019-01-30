package com.kepler.thread;

public class Demo_ProductConsumerOneToOneSetThread extends Thread {

    private Demo_ProductConsumerOneToOneSet setThread;

    public Demo_ProductConsumerOneToOneSetThread(Demo_ProductConsumerOneToOneSet setThread){
        this.setThread = setThread;
    }

    @Override
    public void run() {
        super.run();
        while (true){
            setThread.setValue();
        }
    }
}
