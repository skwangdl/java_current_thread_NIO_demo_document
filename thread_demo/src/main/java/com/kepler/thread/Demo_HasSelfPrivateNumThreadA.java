package com.kepler.thread;

public class Demo_HasSelfPrivateNumThreadA extends Thread {
    private Demo_HasSelfPrivateNum numRef;

    public Demo_HasSelfPrivateNumThreadA(Demo_HasSelfPrivateNum numRef){
        super();
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("a");
    }
}
