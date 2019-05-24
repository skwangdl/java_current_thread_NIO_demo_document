package com.kepler.thread.single_instance;

public class Delay_demo {
    private static Delay_demo delayDemo;

    public static Delay_demo getInstance() throws InterruptedException {
        if(delayDemo == null){
            Thread.sleep(3000);
            synchronized (Delay_demo.class){
                if(delayDemo == null){
                    delayDemo = new Delay_demo();
                }
            }
        }
        return delayDemo;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Delay_demo.getInstance().hashCode());
    }
}
