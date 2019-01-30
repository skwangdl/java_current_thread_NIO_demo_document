package com.kepler.thread;

public class Demo_ProductConsumerOneToOneGet {

    private Object lock;

    public Demo_ProductConsumerOneToOneGet(Object lock){
        this.lock = lock;
    }

    public void getValue(){
        try {
            synchronized (lock){
                if(Demo_ProductConsumerValueObject.value.equals("")){
                    lock.wait();
                }
                System.out.println("get value: " + Demo_ProductConsumerValueObject.value);
                lock.notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
