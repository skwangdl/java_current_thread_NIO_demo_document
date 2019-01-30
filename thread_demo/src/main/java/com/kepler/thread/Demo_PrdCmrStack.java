package com.kepler.thread;

import java.util.ArrayList;
import java.util.List;

public class Demo_PrdCmrStack {
    private List lists = new ArrayList();

    synchronized public void push(){
        try {
            if(lists.size() == 1){
                this.wait();
            }
            lists.add("anything=" + System.currentTimeMillis());
            this.notify();
            System.out.println("push=" + lists.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized public String pop(){
        String returnValue = "";
        try {
            if(lists.size() == 0){
                System.out.println("pop: " + Thread.currentThread().getName());
                this.wait();
            }
            returnValue = "" + lists.get(0);
            lists.remove(0);
            this.notify();
            System.out.println("pop=" + lists.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
