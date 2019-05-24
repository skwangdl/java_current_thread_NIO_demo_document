package com.kepler.current.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class MyService {
    private CountDownLatch countDownLatch = new CountDownLatch(2);

    public void testMethod() throws InterruptedException {
        System.out.println("before wait");
        countDownLatch.countDown();
        System.out.println("after wait");
    }
}
