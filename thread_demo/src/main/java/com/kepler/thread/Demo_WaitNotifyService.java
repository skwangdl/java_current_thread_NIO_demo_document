package com.kepler.thread;

public class Demo_WaitNotifyService {

    private Object lock = new Object();
    private Demo_WaitNotifyServiceList lists = new Demo_WaitNotifyServiceList();

    public void waitMethod(){
        synchronized (lock){
            if(lists.size() != 5 ){
                System.out.println("begin: " + System.currentTimeMillis() +
                        " " + Thread.currentThread().getName());
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("end: " + System.currentTimeMillis() +
                        " " + Thread.currentThread().getName());
            }
        }
    }

    public void notifyMethod(){
        synchronized (lock){
            System.out.println("begin: " + System.currentTimeMillis() +
                    " " + Thread.currentThread().getName());
            for(int i = 0; i < 10; i ++){
                if(lists.size() == 5){
                    lock.notify();
                    System.out.println("notify");
                }
                System.out.println("add num: " + (i + 1));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(" end notify: " + System.currentTimeMillis() +
                    " " + Thread.currentThread().getName());
        }
    }

}
