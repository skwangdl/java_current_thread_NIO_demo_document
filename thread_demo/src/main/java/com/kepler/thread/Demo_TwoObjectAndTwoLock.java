package com.kepler.thread;

public class Demo_TwoObjectAndTwoLock {

    private int num = 0;

    synchronized public void addNum(String username){
        if(username.equals("a")){
            num = 100;
            System.out.println("a set :" + num);
        }else{
            num = 200;
            System.out.println("b set :" + num);
        }
    }
}
