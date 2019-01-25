package com.kepler.thread;

public class Demo_HasSelfPrivateNum{
    int num = 0;

    synchronized public void addI(String username){
        if(username.equals("a")){
            num = 100;
            System.out.println("a set");
        }else{
            num = 200;
            System.out.println("b set");
        }
        System.out.println(username + " :" + num);
    }
}
