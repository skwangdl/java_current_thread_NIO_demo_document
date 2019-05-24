package com.kepler.current.phaser;

import java.util.concurrent.Phaser;

public class MyService {
    private Phaser p1;
    private Phaser p2;

    public MyService(Phaser p1, Phaser p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public void arrivedRoot() throws InterruptedException {
        p1.arriveAndAwaitAdvance();
        Thread.sleep(2000);
        System.out.println(Thread.currentThread().getName() + " root arrive");
    }

    public void arrivedLeef() throws InterruptedException {
        p2.arriveAndAwaitAdvance();
        Thread.sleep(2000);
        System.out.println(Thread.currentThread().getName() + " leef arrive");
    }

}
