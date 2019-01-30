package com.kepler.thread;

public class Demo_PrdCmrStackThreadPush extends Thread {
    private Demo_PrdCmrStack stack;

    public Demo_PrdCmrStackThreadPush(Demo_PrdCmrStack stack){
        this.stack = stack;
    }

    @Override
    public void run() {
        super.run();
        while(true){
            stack.push();
        }
    }
}
