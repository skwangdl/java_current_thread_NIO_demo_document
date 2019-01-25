package com.kepler.thread;

public class Demo_HasSelfPrivateNum{

    public void addI(String username){
        try{
            int num = 0;
            if(username.equals("a")){
                num = 100;
                System.out.println("a set");
                Thread.sleep(2000);
            }else{
                num = 200;
                System.out.println("b set");
            }
            System.out.println(username + " :" + num);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
