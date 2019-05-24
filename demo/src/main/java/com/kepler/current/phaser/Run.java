package com.kepler.current.phaser;

import com.kepler.thread.lock.MyThread;

import java.util.concurrent.Phaser;

public class Run {
    public static void main(String[] args) throws InterruptedException {
        Phaser p1 = new Phaser(1);
        Phaser p2 = new Phaser(p1,2);
        MyService myService = new MyService(p1, p2);
        MyThreadA ta = new MyThreadA(myService);
        MyThreadB tb_1 = new MyThreadB(myService);
        MyThreadB tb_2 = new MyThreadB(myService);
        ta.start();
        tb_1.start();
        tb_2.start();
        Thread.sleep(2000);
        System.out.println("main thread end...");
    }
}
