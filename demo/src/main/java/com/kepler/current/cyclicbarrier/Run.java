package com.kepler.current.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;

public class Run {
    public static void main(String[] args){
        CyclicBarrier cbRef = new CyclicBarrier(5, new Runnable() {
            @Override
            public void run() {
                System.out.println("all coming...");
            }
        });
        MyThread[] threadArray = new MyThread[5];
        for(int i = 0; i < threadArray.length; i ++){
            threadArray[i] = new MyThread(cbRef);
        }
        for(int i = 0; i < threadArray.length; i ++){
            threadArray[i].start();
        }
    }
}
