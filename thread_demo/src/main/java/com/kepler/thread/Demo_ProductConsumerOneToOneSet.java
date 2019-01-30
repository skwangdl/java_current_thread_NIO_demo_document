package com.kepler.thread;

public class Demo_ProductConsumerOneToOneSet {

    private Object lock;

    public Demo_ProductConsumerOneToOneSet(Object lock){
        this.lock = lock;
    }

    public void setValue(){
        try {
            synchronized (lock){
                while(true){
                    if(!Demo_ProductConsumerValueObject.value.equals("")){
                        lock.wait();
                    }
                    String value = System.currentTimeMillis() + " " + System.nanoTime();
                    System.out.println("set value: " + value);
                    Demo_ProductConsumerValueObject.value = value;
                    lock.notifyAll();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
