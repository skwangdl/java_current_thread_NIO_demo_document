package com.kepler.thread;

import org.junit.Test;

public class Demo_ProductConsumerOneToOneRun {

    @Test
    public void run(){
        Object lock = new Object();
        Demo_ProductConsumerOneToOneSet set = new Demo_ProductConsumerOneToOneSet(lock);
        Demo_ProductConsumerOneToOneGet get = new Demo_ProductConsumerOneToOneGet(lock);
        Demo_ProductConsumerOneToOneSetThread setThread = new Demo_ProductConsumerOneToOneSetThread(set);
        Demo_ProductConsumerOneToOneGetThread getThread = new Demo_ProductConsumerOneToOneGetThread(get);
        setThread.run();
        getThread.run();
    }
}
