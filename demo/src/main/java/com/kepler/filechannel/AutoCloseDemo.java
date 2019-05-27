package com.kepler.filechannel;

public class AutoCloseDemo implements AutoCloseable {
    public static void main(String[] args){
        try(AutoCloseDemo dbo = new AutoCloseDemo()){
            System.out.println("demo run...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        System.out.println("auto close..");
    }
}