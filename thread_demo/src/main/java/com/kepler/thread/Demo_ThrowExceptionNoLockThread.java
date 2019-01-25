package com.kepler.thread;

public class Demo_ThrowExceptionNoLockThread extends Thread {
    private Demo_ThrowExceptionNoLock lock;

    public Demo_ThrowExceptionNoLockThread(Demo_ThrowExceptionNoLock lock){
        this.lock = lock;
    }

    @Override
    public void run() {
        super.run();
        try {
            lock.testMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
