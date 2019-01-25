package com.kepler.thread;

public class Demo_HasSelfPrivateNumThreadB extends Thread {
    private Demo_HasSelfPrivateNum numRef;

    public Demo_HasSelfPrivateNumThreadB(Demo_HasSelfPrivateNum numRef){
        super();
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("b");
    }
}
