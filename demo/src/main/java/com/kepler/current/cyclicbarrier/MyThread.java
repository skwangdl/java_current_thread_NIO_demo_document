package com.kepler.current.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MyThread extends Thread {
    private CyclicBarrier cbRef;

    public MyThread(CyclicBarrier cbRef){
        this.cbRef = cbRef;
    }

    @Override
    public void run() {
        try {
            System.out.println("begin first " + Thread.currentThread().getName() +
                    " come.. " + System.currentTimeMillis());
            System.out.println(Thread.currentThread().getName() +
                    " cbRef.await()=" + cbRef.await());
            System.out.println("end first " + Thread.currentThread().getName() +
                    " gone.." + System.currentTimeMillis());

            System.out.println("begin second " + Thread.currentThread().getName() +
                    " come.. " + System.currentTimeMillis());
            System.out.println(Thread.currentThread().getName() +
                    " cbRef.await()=" + cbRef.await());
            System.out.println("end second " + Thread.currentThread().getName() +
                    " gone.." + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
