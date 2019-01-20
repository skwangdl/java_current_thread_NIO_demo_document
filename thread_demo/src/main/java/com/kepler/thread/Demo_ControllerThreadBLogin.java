package com.kepler.thread;

public class Demo_ControllerThreadBLogin extends Thread {

    @Override
    public void run() {
        super.run();
        try {
            Demo_ControllerThreadNoSafe.doPost("b", "bbb");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
