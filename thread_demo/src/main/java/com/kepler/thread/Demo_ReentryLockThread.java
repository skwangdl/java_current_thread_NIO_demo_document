package com.kepler.thread;

public class Demo_ReentryLockThread extends Thread {
    Demo_ReentryLock lock = new Demo_ReentryLock();

    public Demo_ReentryLockThread(Demo_ReentryLock lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        super.run();
        lock.service_1();
    }
}
