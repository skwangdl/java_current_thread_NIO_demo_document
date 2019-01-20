package com.kepler.thread;

import org.junit.Test;

public class Demo_ControllerThreadNoSafeRun {

    @Test
    public void run(){
        Demo_ControllerThreadALogin a = new Demo_ControllerThreadALogin();
        a.start();
        Demo_ControllerThreadBLogin b = new Demo_ControllerThreadBLogin();
        b.start();
    }
}
