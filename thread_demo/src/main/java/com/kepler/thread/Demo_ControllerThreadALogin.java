package com.kepler.thread;

public class Demo_ControllerThreadALogin extends Thread {

    @Override
    public void run() {
        super.run();
        try {
            Demo_ControllerThreadNoSafe.doPost("a", "aaa");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
