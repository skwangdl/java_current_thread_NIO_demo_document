package com.kepler.thread;

public class Demo_PrdCmrStackThreadPop extends Thread {
    private Demo_PrdCmrStack stack;

    public Demo_PrdCmrStackThreadPop(Demo_PrdCmrStack stack){
        this.stack = stack;
    }

    @Override
    public void run() {
        super.run();
        while(true){
            stack.pop();
        }
    }
}
